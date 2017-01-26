package com.mediaplayer.utility;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import java.util.List;

/**
 * Created by shrikanth on 1/22/17.
 */

public class HostRTCCLient extends RTCCLient {



    public HostRTCCLient(List<PeerConnection.IceServer> iceServers) {
        super(iceServers);
    }

    @Override
    public Callbacks getCallbacks() {
        return new RTCCLient.Callbacks() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                Log.d("CLIENT", message.getMessage().toString());
                JsonElement element = message.getMessage();
                JsonObject object = element.getAsJsonObject();
                String device = element.getAsJsonObject().get("device_id").getAsString();
                if (device.equals(RTCCLient.DEVICE_ID)) {
                    return;
                }
                String messageType = object.get(JSONConstants.MESSAGE_TYPE).getAsString();
                switch (messageType){
                    case JSONConstants.SDP:
                        if(!object.has("type")){
                            peer.createOffer(device);
                        }else{
                            String sessionDescription = object.get("description").getAsString();
                            String seesionType = object.get("type").getAsString();
                            if(seesionType.equals(SessionDescription.Type.ANSWER.canonicalForm())){
                                peer.connectionHolder.setRemoteDescription(seesionType, sessionDescription);
                            }
                        }
                        break;
                    case JSONConstants.ICE_CANDIDATE:
                        IceCandidate candidate = new IceCandidate(
                                object.get("sdpMid").getAsString(),
                                object.get("sdpMLineIndex").getAsInt(),
                                object.get("candidate").getAsString()
                        );
                        peer.addIceCandidate(candidate);
                        break;
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        };
    }
}

