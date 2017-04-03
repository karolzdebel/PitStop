package com.karol.pitstop.Cache;

import android.content.Context;

import com.karol.pitstop.API.Location;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Stores and retrieves data from disc
 *
 * Created by Karol Zdebel on 2017-04-02.
 */

public class CacheHelper {

    public static final String CACHE_FILE_NAME = "cacheFile.JSON";

    //Save locations to disc
    public static void save(Context context, ArrayList<Location> data) {

        try {

            File cache = new File(context.getFilesDir(), CACHE_FILE_NAME);

            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(cache));
            out.writeObject(new CacheLocations(data));
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Get String from disc
    public static ArrayList<Location> retrieve(Context context) {

        try {

            File cache = new File(context.getFilesDir(), CACHE_FILE_NAME);

            if (cache.exists()) {

                ObjectInputStream in = new ObjectInputStream(new FileInputStream(cache));
                CacheLocations data = (CacheLocations) in.readObject();
                in.close();

                return data.getLocations();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        //If no file found return null
        return null;
    }

}
