package com.example.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 彬~ on 2017/5/3.
 */

public class City extends DataSupport{
    private int id;
    private String cityName;//城市名字
    private int cityCode;//城市的代码
    private int provinceId;//所在省份的Id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
