package com.karol.pitstop.Activies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.karol.pitstop.API.Location;
import com.karol.pitstop.API.LocationFinder;
import com.karol.pitstop.GUI.BrowseLocationAdapter;
import com.karol.pitstop.R;

import java.util.ArrayList;

/**
 * Displays list of locations to the user and
 *  brief information regarding those locations.
**/

public class BrowseActivity extends AppCompatActivity {

    private ArrayList<Location> location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        //Set title for AppBar
        setTitle(R.string.activity_browse_title);

        //Load all locations
        LocationFinder locationFinder = new LocationFinder(this);
        locationFinder.loadLocations(this);

    }

    //Called by LocationFinder once data is ready
    public void onDataReady(ArrayList<Location> locationList){

        //Store locations for when specific location activities are spawned
        location = locationList;

        //Create adapter
        BrowseLocationAdapter adapter = new BrowseLocationAdapter(this, locationList);

        //Attach adapter to ListView
        ListView listView = (ListView) findViewById(R.id.activity_browse_list_view);

        //Show specific location when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                startLocationActivity(position);
            }
        });

        listView.setAdapter(adapter);

    }

    //Show specific location details to user
    public void startLocationActivity(int index){
        Intent intent = new Intent(this,LocationActivity.class);
        intent.putExtra("location", (Parcelable)location.get(index));
        startActivity(intent);
    }

}
