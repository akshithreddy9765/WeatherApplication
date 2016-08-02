package com.example.akshi.weatherapp.Constants;

/**
 * Created by akshi on 8/1/2016.
 */
public class WeatherUtils {

    public static final String URL_HEAD = "http://api.wunderground.com/api/a0217452a0f01704/conditions/q/";
    public static final String URL_TAIL = ".json";
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    public static final String ENTER_DETAILS_FRAGMENT_TAG = "enterDetailsFragment";

    public static String validateString(String place) {
        if (place != null && place.length() > 0) {
            place = place.trim();
            place = place.replace(" ", "_");
        }

        return place;
    }
}
