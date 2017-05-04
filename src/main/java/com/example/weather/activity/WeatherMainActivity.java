package com.example.weather.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.weather.Base.BaseActivity;
import com.example.weather.R;

/**
 * 显示天气信息的活动界面
 */
public class WeatherMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);
    }

    /**
     * Back按键的逻辑处理
     */
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
