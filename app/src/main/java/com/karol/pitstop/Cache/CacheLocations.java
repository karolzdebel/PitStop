package com.karol.pitstop.Cache;

import com.karol.pitstop.API.Location;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Serializable List of locations used to cache data
 *
 * Created by Karol Zdebel on 2017-04-02.
 */

public class CacheLocations implements Serializable{

    private ArrayList<Location> location;

    public CacheLocations(ArrayList<Location> location){
        this.location = location;
    }

    public ArrayList<Location> getLocations(){
        return location;
    }
}
