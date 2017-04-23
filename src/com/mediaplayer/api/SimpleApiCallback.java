package com.mediaplayer.api;

/**
 * Created by shrikanth on 4/23/17.
 */

public class SimpleApiCallback<T> implements ApiCallbacks<T> {
    @Override
    public void onUpdateData(T data) {

    }

    @Override
    public void onUpdateUI(T data) {

    }

    @Override
    public void onFailure(ApiError error) {

    }
}
