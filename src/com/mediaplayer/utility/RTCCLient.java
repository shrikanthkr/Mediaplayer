package com.mediaplayer.utility;

import android.provider.Settings;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mediaplayer.com.MyApplication;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.webrtc.PeerConnection;

import java.util.List;

/**
 * Created by shrikanth on 1/22/17.
 */

public class RTCCLient {

    Peer peer;
    public final static String COMMON_CHANNEL = "common_channel";
    public final static String DEVICE_ID = Settings.Secure.getString(MyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    public enum MessageType{
        INIT,
        OFFER
    }
    public RTCCLient(List<PeerConnection.IceServer> iceServers) {
        PubNubHelper.initialize(subscribeCallback, publishResultPNCallback);
        PubNubHelper.subscribe();
        peer = new Peer(iceServers, this);
    }

    SubscribeCallback subscribeCallback = new SubscribeCallback() {
        @Override
        public void status(PubNub pubnub, PNStatus status) {

        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {
            Log.d("CLIENT", message.getMessage().toString());
            JsonElement element = message.getMessage();
            String typeName = element.getAsJsonObject().get("message_type").getAsString();
            String device = element.getAsJsonObject().get("device_id").getAsString();
            RTCCLient.MessageType type = RTCCLient.MessageType.valueOf(typeName);
            if(type == MessageType.INIT){
                if(!device.equals(RTCCLient.DEVICE_ID)){
                    peer.createOffer(device);
                }
            }else if(type == MessageType.OFFER){
                JsonObject object = element.getAsJsonObject();
                String seesionType = object.get("type").getAsString();
                String sessionDescription = object.get("description").getAsString();
                peer.createAnswer(device, seesionType, sessionDescription);
            }

        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {

        }
    };

    PNCallback<PNPublishResult> publishResultPNCallback = new PNCallback<PNPublishResult>() {
        @Override
        public void onResponse(PNPublishResult result, PNStatus status) {

        }
    };


    public void transmitMessage(String toId, JsonObject o) {
        PubNubHelper.publish(toId, o);
    }
}
