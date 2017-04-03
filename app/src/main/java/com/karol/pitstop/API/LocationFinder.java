package com.karol.pitstop.API;

import android.content.Context;

import com.karol.pitstop.Activies.BrowseActivity;
import com.karol.pitstop.Cache.CacheHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrieves Location objects from URL and notifies BrowseActivity listener

 * Created by Karol Zdebel on 2017-04-31.
 */

public class LocationFinder {

    private final String jsonURL = "https://api.myjson.com/bins/1vhe1";

    private ArrayList<Location> location; //All locations retrieved from JSON
    private BrowseActivity browseActivity;

    public LocationFinder(BrowseActivity browseActivity){
        this.browseActivity = browseActivity;
    }

    //Load Location List and notify listener
    public void loadLocations(final Context context){

        //Check cache to see if locations are available
        if (CacheHelper.retrieve(context) != null){
            ArrayList<Location> data = CacheHelper.retrieve(context);

            //Send data to browse activity listener
            browseActivity.onDataReady(data);

        }

        else{

            //No cache so make API call and get the JSON file
            getLocations(new Callback<List<Location>>() {
                @Override
                public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {

                    //Store data in cache
                    CacheHelper.save(context, new ArrayList<Location>(response.body()));

                    //Send data over to BrowseActivity for display as ArrayList
                    browseActivity.onDataReady(new ArrayList<Location>(response.body()));
                }

                @Override
                public void onFailure(Call<List<Location>> call, Throwable throwable) {
                }
            });
        }
    }

    public Location getLocation(int index){
        return location.get(index);
    }

    public ArrayList<Location> getAllLocations(){
        return location;
    }

    //Make API call to retrieve Locations
    private void getLocations(Callback<List<Location>> callback){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.myjson.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LocationAPI api = retrofit.create(LocationAPI.class);

        Call<List<Location>> call = api.getLocations(jsonURL);
        call.enqueue(callback);
    }

}
