package com.karol.pitstop.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * API formatting
 *
 * Created by Karol Zdebel on 2017-04-01.
 */

public interface LocationAPI {

    @GET
    Call<List<Location>> getLocations(
        @Url String location
    );
}
