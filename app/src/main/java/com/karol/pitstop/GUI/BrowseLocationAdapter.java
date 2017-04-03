package com.karol.pitstop.GUI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karol.pitstop.API.Location;
import com.karol.pitstop.R;

import java.util.ArrayList;

/**
 * Uses List of Location objects to fill a ListView with
 *  the provided locations information.
 *
 * Created by Karol Zdebel on 2017-04-01.
 */

public class BrowseLocationAdapter extends ArrayAdapter<Location> {

    public BrowseLocationAdapter(Context context, ArrayList<Location> location){
        super(context,0,location);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Get data item
        Location location = getItem(position);

        //Check if view is being reused
        if (convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_browse_location,parent,false);
        }

        //Set title
        TextView title = (TextView) convertView.findViewById(R.id.activity_browse_location_name);
        title.setText(location.getTitle());

        //Set whether the location is open
        TextView open = (TextView) convertView.findViewById(R.id.activity_browse_locaton_open);
        if (location.isOpen()){
            open.setText(R.string.open);
            open.setTextColor(Color.GREEN);
        }
        else{
            open.setText(R.string.closed);
            open.setTextColor(Color.RED);
        }

        return convertView;
    }
}
