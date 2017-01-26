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

public class Peer implements SdpObserver, PeerConnection.Observer, DataChannel.Observer{

    private static final String TAG = "Peer";
    RTCCLient rtcClient;
    PeerConnectionHolder connectionHolder;
    String toId;
    DataChannel.Init dc = new  DataChannel.Init();
    DataChannel dataChannel;

    public Peer(List<PeerConnection.IceServer> iceServers, RTCCLient rtcClient) {
        this.rtcClient = rtcClient;
        connectionHolder = new PeerConnectionHolder(this, iceServers, dc);
    }

    //SDB observer
    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        JsonObject o = new JsonObject();
        o.addProperty("type", sessionDescription.type.canonicalForm());
        o.addProperty(JSONConstants.MESSAGE_TYPE, JSONConstants.SDP);
        o.addProperty("description", sessionDescription.description);
        o.addProperty("device_id", RTCCLient.DEVICE_ID);
        connectionHolder.getPc().setLocalDescription(this, sessionDescription);
        rtcClient.transmitMessage(toId, o);
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
        if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
            Log.d(TAG, "iceConnectionState disconnected");
        }
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Log.d(TAG, "iceGatheringState Change");
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        Log.d(TAG, "iceCandidate Change");
        JsonObject payload = new JsonObject();
        payload.addProperty(JSONConstants.MESSAGE_TYPE, JSONConstants.ICE_CANDIDATE);
        payload.addProperty("sdpMLineIndex", iceCandidate.sdpMLineIndex);
        payload.addProperty("sdpMid", iceCandidate.sdpMid);
        payload.addProperty("candidate", iceCandidate.sdp);
        payload.addProperty("device_id", RTCCLient.DEVICE_ID);
        rtcClient.transmitMessage(toId, payload);
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        Log.d(TAG, "onAddStream Change");
        Log.d(TAG, "Audio Track" + mediaStream.audioTracks.size());
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        Log.d(TAG, "onRemoveStream Change");
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        Log.d(TAG, "onDataChannel Change");
        this.dataChannel = dataChannel;
    }

    @Override
    public void onRenegotiationNeeded() {
        Log.d(TAG, "onRenegotiationNeeded Change");
    }



    public void createOffer(String device){
        toId = device;
        connectionHolder.createOffer();
    }

    public void createAnswer(String s, String seesionType, String sesionDescription) {
        toId = s;
        connectionHolder.createAnswer(seesionType, sesionDescription);
    }

    public void addIceCandidate(IceCandidate iceCandidate){
        connectionHolder.pc.addIceCandidate(iceCandidate);
    }

    @Override
    public void onStateChange() {
        Log.d(TAG, "DataChannel: onStateChange: " + dataChannel.state());
    }

    @Override
    public void onMessage(DataChannel.Buffer buffer) {
        Log.d(TAG, "DataChannel: onStateChange: " + buffer.toString());
    }
}
