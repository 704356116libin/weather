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
}
