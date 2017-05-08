package com.example.weather.json;

/**
 * Created by 彬~ on 2017/5/8.
 */

public class hourly_forecast {
    public cond cond;
    public String data;//时间
    public String hum;//相对湿度
    public String pop;//降水概率
    public String tmp;//温度
    public wind wind;
    public class cond{
        public String code;//天气状况代码
        public String txt;//天气描述
    }
    public class wind{
        public String dir;//风向
        public String sc;//风力
        public String spd;//风速
    }
}
