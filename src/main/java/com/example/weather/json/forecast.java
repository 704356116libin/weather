package com.example.weather.json;

/**
 * 三天天气预报
 * Created by 彬~ on 2017/5/4.
 */

public class forecast {
    public cond cond;
    public String date;//拿到预报日期
    public tmp tmp;
    public class tmp{
        public String max;//最高温度
        public String min;//最低温度
    }
    public class cond{
        public String code_d;//拿到白天天气状态代码
        public String txt_d;//拿到白天天气描述
    }
}
