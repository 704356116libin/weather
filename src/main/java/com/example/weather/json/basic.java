package com.example.weather.json;

/**
 * Created by 彬~ on 2017/5/4.
 */

public class basic {
    public String city;//拿到城市名
    public String cnty;//拿到国家名字
    public String prov;//拿到所属省份名字
    public String id;//拿到天气id
    public update update;
    public class update{
        public String loc;//当地时间
    }
}
