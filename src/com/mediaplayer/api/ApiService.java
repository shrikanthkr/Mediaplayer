package com.mediaplayer.api;

import com.mediaplayer.models.RadioStation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shrikanth on 4/22/17.
 */

public interface ApiService {
    @GET("stations")
    Call<List<RadioStation>> getStations();
}
