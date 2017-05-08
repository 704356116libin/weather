package com.example.weather.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 天气实体类
 * Created by 彬~ on 2017/5/5.
 */

public class weather {
    public String status;
    public aqi aqi;
    public basic basic;
    @SerializedName("daily_forecast")
    public List<forecast>daily_forecast;
    public now now;
    public suggestion suggestion;
}
