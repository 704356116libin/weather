package com.example.weather.json;

/**
 * 现在天气
 * Created by 彬~ on 2017/5/4.
 */

public class now {
    public cond cond;
    public wind wind;
    public String fl;//体感温度
    public String hum;//相对湿度
    public class wind{
        public String dir;//风向
        public String sc;//风力
    }
    public class  cond{
        public String code;//天气状况代码
        public String txt;//天气状况描述

    }
}
