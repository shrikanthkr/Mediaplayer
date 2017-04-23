package com.mediaplayer.api;

/**
 * Created by shrikanth on 4/23/17.
 */

public interface ApiCallbacks<T extends Object> {
    //Happens background
    void onUpdateData(T data);

    //called on UI thread
    void onUpdateUI(T data);

    //called on UI thread
    void onFailure(ApiError error);
}
