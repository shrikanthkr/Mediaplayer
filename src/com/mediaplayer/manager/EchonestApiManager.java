package com.mediaplayer.manager;

import com.echonest.api.v4.Track;
import com.mediaplayer.com.IdentifyActivityThread;

import org.json.JSONObject;

/**
 * Created by shrikanth on 10/4/15.
 */
public class EchonestApiManager {
    public static void uploadTrack(String path,EchonestApiListener listener){
        new IdentifyActivityThread(path,listener).execute();
    }

    public interface EchonestApiListener{
        void onResult(Track track);
    }
}
