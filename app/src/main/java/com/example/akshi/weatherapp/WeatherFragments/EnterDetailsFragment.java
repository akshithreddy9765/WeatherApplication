package com.example.akshi.weatherapp.WeatherFragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.akshi.weatherapp.AppController;
import com.example.akshi.weatherapp.Constants.WeatherUtils;
import com.example.akshi.weatherapp.GetCurrentLocation;
import com.example.akshi.weatherapp.Interfaces.IWeatherTaskCallback;
import com.example.akshi.weatherapp.LocationData;
import com.example.akshi.weatherapp.MainActivity;
import com.example.akshi.weatherapp.R;
import com.example.akshi.weatherapp.WeatherModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EnterDetailsFragment extends Fragment implements View.OnClickListener {

    private EditText mCityET, mStateET;
    private Button mGetDataBtn, mLocationBtn;
    private IWeatherTaskCallback taskCallback;
    private ProgressDialog dialog;
    private Context context;

    public void setWeatherTask(Context context, IWeatherTaskCallback taskCallback) {
        this.taskCallback = taskCallback;
        this.context = context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_details, container, false);

        mCityET = (EditText) view.findViewById(R.id.cityET);
        mStateET = (EditText) view.findViewById(R.id.stateET);
        mGetDataBtn = (Button) view.findViewById(R.id.dataButton);
        mLocationBtn = (Button) view.findViewById(R.id.current_loc);

        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        mLocationBtn.setOnClickListener(this);
        mGetDataBtn.setOnClickListener(this);
        dialog = new ProgressDialog(context);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.current_loc:
                showDialog();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, WeatherUtils.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    GetCurrentLocation getCurrentLocation = new GetCurrentLocation(getActivity());
                    getCurrentLocation.getLocation();
                }


                List<Address> addressList = LocationData.getInstance().getAddressList();
                String currentCity, currentState;
                if (addressList != null && addressList.size() > 0) {
                    currentCity = WeatherUtils.validateString(addressList.get(0).getLocality());
                    currentState = WeatherUtils.validateString(addressList.get(0).getAdminArea());

                    getLocationData(currentCity, currentState);


                }

                break;
            case R.id.dataButton:
                showDialog();
                String city = WeatherUtils.validateString(mCityET.getText().toString());
                String state = WeatherUtils.validateString(mStateET.getText().toString());
                if (city.length() > 1 && state.length() > 1) {
                    getLocationData(city, state);
                } else {
                    hideDialog();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Please Enter a valid Details");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                }
                break;
        }
    }

    public void getLocationData(String city, String state) {

        String currentUrl = WeatherUtils.URL_HEAD + state + "/" + city + WeatherUtils.URL_TAIL;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, currentUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                Log.i(EnterDetailsFragment.class.getSimpleName(), "the response from the server is: " + response);
                parseResponse(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(context, "Please enter Valid City and State", Toast.LENGTH_LONG).show();

            }

        });

        AppController.getInstance().addToRequestQueue(objectRequest);
    }


    public void parseResponse(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject observation_jsonObject = jsonObject.getJSONObject("current_observation");
            WeatherModel weatherModel = new WeatherModel();
            if (observation_jsonObject.has("temp_f")) {
                String temp_f = observation_jsonObject.getString("temp_f");
                weatherModel.setTemp_f(temp_f);
            }
            if (observation_jsonObject.has("temp_c")) {
                String temp_c = observation_jsonObject.getString("temp_c");
                weatherModel.setTemp_c(temp_c);
            }

            if (observation_jsonObject.has("icon")) {
                String icon = observation_jsonObject.getString("icon");
                weatherModel.setIcon(icon);
            }

            if (observation_jsonObject.has("icon_url")) {
                String icon_url = observation_jsonObject.getString("icon_url");
                weatherModel.setIcon_url(icon_url);
            }

            JSONObject display_location = observation_jsonObject.getJSONObject("display_location");
            if (display_location.has("full")) {
                String name = display_location.getString("full");
                weatherModel.setFullName(name);
            }

            if (taskCallback != null) {
                taskCallback.getWeatherData(weatherModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void showDialog() {
        dialog.setMessage("Loading Please Wait...");
        dialog.show();
    }

    protected void hideDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WeatherUtils.MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    GetCurrentLocation getLocation = new GetCurrentLocation(getActivity());
                    getLocation.getLocation();


                } else {
                    Toast.makeText(getActivity(), getString(R.string.accept_permission), Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            WeatherUtils.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
