package com.mediaplayer.api;

import android.os.AsyncTask;

import com.mediaplayer.fragments.BaseFragment;

import java.io.IOException;
import java.lang.ref.SoftReference;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by shrikanth on 4/23/17.
 */

public class AsyncApiLoader<T>  extends AsyncTask<Void, Void, T> {

    AsyncApiTask<T> task;
    SoftReference<ApiCallbacks<T>> callbacks;
    SoftReference<BaseFragment> fragment;
    ApiError error;
    Call<T> call = null;
    public AsyncApiLoader(BaseFragment fragment, ApiCallbacks<T> callbacks, AsyncApiTask<T>  task) {
        this.task = task;
        this.callbacks = new SoftReference<>(callbacks);
        this.fragment = new SoftReference<>(fragment);
    }
    public AsyncApiLoader(ApiCallbacks<T> callbacks, AsyncApiTask<T> task){
        this.task = task;
        this.callbacks = new SoftReference<>(callbacks);
    }

    @Override
    protected T doInBackground(Void... params) {
        try {
            call = task.execute();
            Response<T> response = call.execute();
            if(callbacks.get() != null) {
                if(response.isSuccessful()) {
                    callbacks.get().onUpdateData(response.body());
                }else{
                    String message = response.errorBody().string();
                    error = new ApiError((message));
                }
                return response.body();
            }
        } catch (IOException e) {
            String message = "No Network";
            error = new ApiError((message));
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(T data) {
        super.onPostExecute(data);
        if(canUpdateUI()) {
            if(error == null) {
                callbacks.get().onUpdateUI(data);
            }else{
                callbacks.get().onFailure(error);
            }
        }
    }

    private boolean canUpdateUI(){
        return fragment != null && fragment.get() != null && fragment.get().isResumed() && callbacks.get() != null;
    }

    public interface AsyncApiTask<T>{
        Call<T> execute() throws IOException;
    }
}
