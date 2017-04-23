package com.mediaplayer.api;

import com.mediaplayer.fragments.BaseFragment;
import com.mediaplayer.models.RadioStation;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import stage.EnvConfig;

/**
 * Created by shrikanth on 4/23/17.
 */

public class Api {

    private  BaseFragment baseFragment;

    private static final ApiService service = new Retrofit.Builder()
            .baseUrl(EnvConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(ApiService.class);

    public Api(BaseFragment fragment) {
        this.baseFragment = fragment;
    }

    public void getStations(ApiCallbacks<List<RadioStation>>  callback){
        AsyncApiLoader<List<RadioStation>> apiLoader = new AsyncApiLoader<>(baseFragment, callback, new AsyncApiLoader.AsyncApiTask<List<RadioStation>>() {
            @Override
            public Call<List<RadioStation>> execute() throws IOException {
                return service.getStations();
            }
        });
        apiLoader.execute();
    }


}
