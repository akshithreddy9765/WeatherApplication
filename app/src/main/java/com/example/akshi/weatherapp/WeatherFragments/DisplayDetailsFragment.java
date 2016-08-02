package com.example.akshi.weatherapp.WeatherFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.akshi.weatherapp.R;
import com.example.akshi.weatherapp.WeatherModel;
import com.squareup.picasso.Picasso;


public class DisplayDetailsFragment extends Fragment {

    private TextView location_text;
    private ImageView icon_image;
    private TextView icon_text;
    private WeatherModel weatherModel;

    public void setWeatherData(WeatherModel weatherModel) {
        this.weatherModel = weatherModel;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display_details, container, false);

        location_text = (TextView) view.findViewById(R.id.textView);
        icon_text = (TextView) view.findViewById(R.id.icon_text);
        icon_image = (ImageView) view.findViewById(R.id.icon_image);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setRetainInstance(true);
        if (weatherModel != null) {
            String result = "Location: " + weatherModel.getFullName() + "\n" + "temp_c: " + weatherModel.getTemp_c() + "\n" + "temp_f: " + weatherModel.getTemp_f();
            location_text.setText(result);
            icon_text.setText(weatherModel.getIcon());
            Picasso.with(getActivity()).load(weatherModel.getIcon_url()).into(icon_image);
        }

    }
}
