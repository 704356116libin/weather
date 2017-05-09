package com.example.weather.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.weather.Base.BaseActivity;
import com.example.weather.R;

/**
 * g该活动用来选择城市信息
 */
public class Weather extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

    }

    @Override
    public void onBackPressed() {
        SharedPreferences weatherPrefercnce=Weather.this.getSharedPreferences("weather", Context.MODE_PRIVATE);
        String id=weatherPrefercnce.getString("weather_id",null);
        if (id==null){
            super.onBackPressed();
        }else {
            WeatherMainActivity.actionStartActivity(Weather.this, id);
            finish();
        }
    }
}
