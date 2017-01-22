package com.mediaplayer.utility;

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrikanth on 1/22/17.
 */

public class PubNubHelper {
    static final private String PUB_KEY = "pub-c-1221ec96-3d4a-4843-927c-1020dd92554e";
    final static private String SUB_KEY = "sub-c-7235fc50-e074-11e6-8652-02ee2ddab7fe";
    static private PNConfiguration pnConfiguration = new PNConfiguration();
    static PubNub pb;
    private static List<String> channels = new ArrayList<>();
    private static SubscribeCallback pnCallbacks;
    private static PNCallback<PNPublishResult> publishResultPNCallback;

    public static void initialize(SubscribeCallback callbacks, PNCallback<PNPublishResult> publishResultPNCallback){
        PubNubHelper.pnConfiguration.setSubscribeKey(SUB_KEY);
        PubNubHelper. pnConfiguration.setPublishKey(PUB_KEY);
        PubNubHelper.pnConfiguration.setSecure(false);
        PubNubHelper.pb = new PubNub(pnConfiguration);
        PubNubHelper.pnCallbacks = callbacks;
        PubNubHelper.publishResultPNCallback = publishResultPNCallback;
        PubNubHelper.channels.add(RTCCLient.COMMON_CHANNEL);
        PubNubHelper.channels.add(RTCCLient.DEVICE_ID);
    }

    public static void subscribe(){
        pb.addListener(pnCallbacks);
        pb.subscribe().channels(channels).execute();
    }

    public static void publish(String channel, JsonObject object){
        pb.publish()
            .message(object)
            .channel(channel)
            .async(publishResultPNCallback);
    }


}
