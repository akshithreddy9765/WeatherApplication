package com.example.akshi.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.akshi.weatherapp.Constants.WeatherUtils;
import com.example.akshi.weatherapp.Interfaces.IWeatherTaskCallback;
import com.example.akshi.weatherapp.WeatherFragments.DisplayDetailsFragment;
import com.example.akshi.weatherapp.WeatherFragments.EnterDetailsFragment;

public class MainActivity extends AppCompatActivity {

    private EnterDetailsFragment enterDetailsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterDetailsFragment = new EnterDetailsFragment();
        enterDetailsFragment.setWeatherTask(MainActivity.this, taskCallback);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainContainer, enterDetailsFragment, WeatherUtils.ENTER_DETAILS_FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    IWeatherTaskCallback taskCallback = new IWeatherTaskCallback() {
        @Override
        public void getWeatherData(WeatherModel weatherModel) {

            DisplayDetailsFragment displayDetailsFragment = new DisplayDetailsFragment();
            displayDetailsFragment.setWeatherData(weatherModel);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContainer, displayDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    };


}
