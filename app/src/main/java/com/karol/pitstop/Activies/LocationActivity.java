package com.karol.pitstop.Activies;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karol.pitstop.API.Location;
import com.karol.pitstop.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Displays a locations information to the user.
 *
 * Created by Karol Zdebel on 2017-03-31.
 */

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int MAP_HEIGHT_DP = 250;  //height of the map in dp units

    private Location location;  //location data for display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //Retrieve location object from calling activity
        Intent intent = getIntent();
        location = intent.getExtras().getParcelable("location");

        //Set title for AppBar
        setTitle(location.getTitle());

        //Populate all views
        populateView();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in the location currently being viewed
        // and move the map's camera to the same location.
        try{
            Geocoder geo = new Geocoder(this);

            //Get address provided for location as Address object
            List<Address> foundAddresses = geo.getFromLocationName(location.getAddress(),1);
            Address address = geo.getFromLocationName(location.getAddress(),1).get(0);

            LatLng position= new LatLng(address.getLatitude(),address.getLongitude());

            googleMap.addMarker(new MarkerOptions().position(position)
                    .title(location.getTitle()));
            googleMap.setMinZoomPreference(12);
            googleMap.setMaxZoomPreference(15);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    //Returns whether Address object can be created for location address String
    private boolean canFindLocationAddress(){

        Geocoder geo = new Geocoder(this);

        //Get address provided for location as Address object
        try{
            List<Address> foundAddresses = geo.getFromLocationName(location.getAddress(),1);
            //Return true if an address is found
            return (foundAddresses.size() == 1);
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }

    }

    //Populate all activity views based on available resources
    private void populateView(){

        //Display image without blocking, and cache it
        ImageView imageView = (ImageView) findViewById(R.id.activity_location_image);
        Picasso.with(this)
                .load(location.getImageURL()).fit().centerCrop().into(imageView);

        //Display description
        TextView description = (TextView) findViewById(R.id.activity_location_description);
        description.setText(location.getDescription());

        //Display hours
        TextView hours = (TextView) findViewById(R.id.activity_location_hours);
        hours.setText(location.getStringOpenHours());

        //Display google maps, map IF address can be found
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_location_map);
        if (canFindLocationAddress()){

            //Set width and height of map
            mapFragment.getView().getLayoutParams().height = getMapHeight();
            mapFragment.getView().offsetLeftAndRight(50);
            mapFragment.getMapAsync(this);
        }

        //Hide the map otherwise
        else{
            mapFragment.getView().setVisibility(View.INVISIBLE);
        }
    }

    private int getMapHeight(){
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (MAP_HEIGHT_DP * scale + 0.5f);
    }

}
