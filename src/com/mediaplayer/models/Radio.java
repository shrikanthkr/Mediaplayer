package com.mediaplayer.models;



/**
 * Created by shrikanth on 2/18/17.
 */

public class Radio extends BaseModel {

    public Radio() {
    }

    public Radio(String streamUrl, String streamTitle) {
        this.streamUrl = streamUrl;
        this.streamTitle = streamTitle;
    }

    public String streamUrl;
    public String streamTitle;
}
