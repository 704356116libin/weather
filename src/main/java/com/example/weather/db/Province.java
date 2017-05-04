package com.example.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 彬~ on 2017/5/3.
 */

public class Province extends DataSupport{
    private int id;
    private String provinceName;//省份名字的字段
    private int provinceCode;//省份代码字段

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
