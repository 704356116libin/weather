package com.example.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 彬~ on 2017/5/3.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;//县市的名字
    private int cityId;//所属城市的名字
    private String weather_id;//访问天气的id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }
}
