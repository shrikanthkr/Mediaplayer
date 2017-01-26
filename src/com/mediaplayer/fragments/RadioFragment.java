package com.mediaplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.utility.HostRTCCLient;
import com.mediaplayer.utility.JSONConstants;
import com.mediaplayer.utility.RTCCLient;
import com.mediaplayer.utility.ReceiverRTCCLient;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.List;

public class RadioFragment extends BaseFragment {


	final String TAG = "JINGRY";
	SongInfo info;
	enum Mode {
		HOST, RECEIVER
	}
	Mode currentMode;
	RTCCLient rtccLient;
	List<PeerConnection.IceServer> iceServers;

	Button host, receiver;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.radio_fragment,container,false);
		v.setVisibility(View.GONE);
		this.iceServers = defaultIceServers();
		setupView(v);
		info = SongInfoDatabase.getInstance().getSongs("0").get(0);
		return v;
	}



	private void setupView(View v) {
		v.setVisibility(View.VISIBLE);
		host = (Button)v.findViewById(R.id.host);
		receiver = (Button)v.findViewById(R.id.receiver);


		host.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setupHost();
			}
		});

		receiver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setupClient();
			}
		});
	}

	private void setupHost(){
		currentMode = Mode.HOST;
		rtccLient = new HostRTCCLient(iceServers);
	}

	private void setupClient(){
		rtccLient = new ReceiverRTCCLient(iceServers);
		currentMode = Mode.RECEIVER;
		JsonObject object = new JsonObject();
		object.addProperty("device_id", RTCCLient.DEVICE_ID);
		object.addProperty(JSONConstants.MESSAGE_TYPE, JSONConstants.SDP);
		rtccLient.transmitMessage(RTCCLient.COMMON_CHANNEL, object);
	}

	@Override
	public void setTitle() {
		getActivity().setTitle(getString(R.string.songs));
	}

	public static List<PeerConnection.IceServer> defaultIceServers(){
		List<PeerConnection.IceServer> iceServers = new ArrayList<PeerConnection.IceServer>(25);
		iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
		iceServers.add(new PeerConnection.IceServer("stun:stun.services.mozilla.com"));
		iceServers.add(new PeerConnection.IceServer("turn:turn.bistri.com:80", "homeo", "homeo"));
		iceServers.add(new PeerConnection.IceServer("turn:turn.anyfirewall.com:443?transport=tcp", "webrtc", "webrtc"));


		return iceServers;
	}
}
