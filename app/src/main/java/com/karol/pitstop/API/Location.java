package com.karol.pitstop.API;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Holds information regarding a specific location
 *  and provides inference based on that information
 *
 * Created by Karol Zdebel on 2017-03-31.
 */

public class Location implements Parcelable,Serializable {

    private String title; //location title
    private String description; //description of location
    private String address; //address of location
    private String image; //location image URL
    private String url; //URL of the locations website
    private Map<String,String> hours; //mapping of each weekday to a range of hours

    static class Day{
        static final String HOUR_FORMAT_LONG = "hh:mma";
        static final String MONDAY = "Monday";
        static final String TUESDAY = "Tuesday";
        static final String WEDNESDAY = "Wednesday";
        static final String THURSDAY = "Thursday";
        static final String FRIDAY = "Friday";
        static final String SATURDAY = "Saturday";
        static final String SUNDAY = "Sunday";
    }

    //Regular constructor
    public Location(String title,String description, String address, String image, Map<String,String> hours){
        this.title = title;
        this.description = description;
        this.address = address;
        this.image = image;
        this.hours = hours;
    }

    //Parcel constructor
    public Location(Parcel p){
        title = p.readString();
        description = p.readString();
        address = p.readString();
        image = p.readString();
        url = p.readString();
        hours = new HashMap<>();
        p.readMap(hours,String.class.getClassLoader());
    }

    //Method for creating object as parcel
    public static final Parcelable.Creator<Location> CREATOR
            = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeString(image);
        dest.writeString(url);
        dest.writeMap(hours);
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getImageURL() {
        return image;
    }

    public Map<String, String> getHours() {
        return hours;
    }

    public String getLocationURL() {
        return url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImageURL(String image) {
        this.image = image;
    }

    public void setLocationURL(String url) {
        this.url = url;
    }

    public void setHours(Map<String, String> hours) {
        this.hours = hours;
    }

    public String toString(){
        return "title:"+title+"  "+"address:"+address;
    }

    //Returns whether location is currently open
    public boolean isOpen() {

        //Get todays opening and closing hours
        String todaysHoursString = hours.get(getWeekday());

        //Get the hours into correct format
        String[] hourBounds = formatHourBound(todaysHoursString.split("-"));

        //Check if closed today
        if (hourBounds.length == 1){
            return false;
        }

        String openTime = hourBounds[0];
        String closeTime = hourBounds[1];

        //Check if current time is between open and closing time
        if (compareTime(openTime,getTime()) && compareTime(getTime(),closeTime)){
            return true;
        }

        return false;
    }

    public String getStringOpenHours(){
        String ret = "Hours of Operation:\n";

        for (Map.Entry<String,String> entry: hours.entrySet()){
            ret += entry.getKey() + ": "+entry.getValue()+"\n";
        }

        return ret;
    }

    //Get hours into correct format
    private String[] formatHourBound(String[] hourBound){
        String[] formattedHourBound = new String[hourBound.length];

        //Go through each hour bound
        for (int i=0;i<hourBound.length;i++){

            //Modify string only if it is not in the correct format
            if (hourBound[i].length() == 3){

                //Add minutes
                formattedHourBound[i] = hourBound[i].charAt(0) + ":00"
                        +hourBound[i].substring(hourBound[i].length()-2,hourBound[i].length());

                //Add 0 if the hour is between 0-9
                formattedHourBound[i] = "0"+formattedHourBound[i];
            }

            else if (hourBound[i].length() == 4){
                formattedHourBound[i] = hourBound[i].substring(0,2)
                        +":00"+hourBound[i].substring(hourBound[i].length()-2,hourBound[i].length());
            }
            else{
                formattedHourBound[i] = hourBound[i];
            }
        }

        return formattedHourBound;
    }

    //Returns true if a comes before b, false otherwise
    private boolean compareTime(String a, String b){
        String pattern = "hh:mma";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(a);
            Date date2 = sdf.parse(b);

            if(date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    //Return the current time in format
    private String getTime(){
        SimpleDateFormat format = new SimpleDateFormat(Day.HOUR_FORMAT_LONG);

        return format.format(Calendar.getInstance().getTime());
    }

    //Returns string version of current weekday
    private String getWeekday(){
        return Calendar.getInstance()
                .getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.CANADA);
    }

}
