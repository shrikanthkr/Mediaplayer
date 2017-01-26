package com.mediaplayer.utility;

import android.provider.Settings;

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

public abstract class RTCCLient {

    Peer peer;
    public final static String COMMON_CHANNEL = "common_channel";
    public final static String DEVICE_ID = Settings.Secure.getString(MyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    Callbacks callbacks;

    public RTCCLient(List<PeerConnection.IceServer> iceServers) {
        this.callbacks = getCallbacks();
        PubNubHelper.initialize(subscribeCallback, publishResultPNCallback);
        PubNubHelper.subscribe();
        peer = new Peer(iceServers, this);
    }

    SubscribeCallback subscribeCallback = new SubscribeCallback() {
        @Override
        public void status(PubNub pubnub, PNStatus status) {
            callbacks.status(pubnub, status);
        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {
            callbacks.message(pubnub, message);
        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            callbacks.presence(pubnub, presence);
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

    public abstract Callbacks getCallbacks();


    public interface Callbacks{
        public void status(PubNub pubnub, PNStatus status);
        public void message(PubNub pubnub, PNMessageResult message);
        public void presence(PubNub pubnub, PNPresenceEventResult presence);
    }
}
