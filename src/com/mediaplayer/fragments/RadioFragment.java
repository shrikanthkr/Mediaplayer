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
import com.mediaplayer.utility.RTCCLient;
import com.mediaplayer.utility.XirSysRequest;

import org.webrtc.PeerConnection;

import java.util.List;

public class RadioFragment extends BaseFragment {


	final String TAG = "JINGRY";
	SongInfo info;
	enum Mode {
		HOST, RECEIVER
	}
	Mode currentMode;
	RTCCLient rtccLient;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		XirSysRequest request = new XirSysRequest(new XirSysRequest.XirsysCallback() {
			@Override
			public void setIceServers(List<PeerConnection.IceServer> iceServers) {
				rtccLient = new RTCCLient(iceServers);
			}
		});
		request.execute();
		info = SongInfoDatabase.getInstance().getSongs("0").get(0);
	}


	Button sub, pub;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.radio_fragment,container,false);
		sub = (Button)v.findViewById(R.id.sub);
		pub = (Button)v.findViewById(R.id.pub);


		sub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentMode = Mode.HOST;
				setupHost();
			}
		});

		pub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentMode = Mode.RECEIVER;
				JsonObject object = new JsonObject();
				object.addProperty("device_id", RTCCLient.DEVICE_ID);
				object.addProperty("message_type", RTCCLient.MessageType.INIT.name());
				rtccLient.transmitMessage(RTCCLient.COMMON_CHANNEL, object);
			}
		});
		return v;
	}

	private void setupHost() {
		//peer.startStream(info.getData());
	}

	@Override
	public void setTitle() {
		getActivity().setTitle(getString(R.string.songs));
	}

}
