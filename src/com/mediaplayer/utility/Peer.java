package com.mediaplayer.utility;

import android.util.Log;

import com.google.gson.JsonObject;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.List;

/**
 * Created by shrikanth on 1/22/17.
 */

public class Peer implements SdpObserver, PeerConnection.Observer{

    private static final String TAG = "Peer";
    RTCCLient rtcClient;
    PeerConnectionHolder connectionHolder;
    String toId;

    public Peer(List<PeerConnection.IceServer> iceServers, RTCCLient rtcClient) {
        this.rtcClient = rtcClient;
        connectionHolder = new PeerConnectionHolder(this, iceServers);
    }

    //SDB observer
    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        JsonObject o = new JsonObject();
        o.addProperty("type", sessionDescription.type.canonicalForm());
        o.addProperty("message_type", RTCCLient.MessageType.OFFER.name());
        o.addProperty("description", sessionDescription.description);
        o.addProperty("device_id", RTCCLient.DEVICE_ID);
        rtcClient.transmitMessage(toId, o);
        connectionHolder.getPc().setLocalDescription(this, sessionDescription);
    }

    @Override
    public void onSetSuccess() {

    }

    @Override
    public void onCreateFailure(String s) {

    }

    @Override
    public void onSetFailure(String s) {

    }

    //peer connection observers

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        Log.d(TAG, "Signal Change");
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        Log.d(TAG, "iceConnectionState Change");
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Log.d(TAG, "iceGatheringState Change");
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        Log.d(TAG, "iceCandidate Change");
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        Log.d(TAG, "onAddStream Change");
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        Log.d(TAG, "onRemoveStream Change");
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        Log.d(TAG, "onDataChannel Change");
    }

    @Override
    public void onRenegotiationNeeded() {
        Log.d(TAG, "onRenegotiationNeeded Change");
    }


    public void startStream(String audioId){
        MediaStream stream = connectionHolder.getMediaStream();
    }

    public void createOffer(String device){
        toId = device;
        connectionHolder.createOffer();
    }

    public void createAnswer(String s, String seesionType, String sesionDescription) {
        toId = s;
        connectionHolder.createAnswer(seesionType, sesionDescription);
    }
}
