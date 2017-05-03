package com.example.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.weather.Base.BaseActivity;

public class WeatherMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);
    }
}
