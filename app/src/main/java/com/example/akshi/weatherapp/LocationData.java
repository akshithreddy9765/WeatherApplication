package com.example.akshi.weatherapp;

import android.location.Address;

import java.util.List;

/**
 * Created by akshi on 8/2/2016.
 */
public class LocationData {

    private static LocationData mInstance = new LocationData();


    private List<Address> addressList;

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    private LocationData() {

    }

    public static LocationData getInstance() {
        return mInstance;
    }


}
