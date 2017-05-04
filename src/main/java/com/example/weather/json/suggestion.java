package com.example.weather.json;

/**接收天气数据的Suggestion实体类
 * Created by 彬~ on 2017/5/4.
 */

public class suggestion {
    public comf comf;
    public drsg drsg;
    public flu flu;
    public sport sport;

    /**
     * 身体感觉
     */
    public class comf{
        public String brf;
        public String txt;
    }

    /**
     * 穿衣指数
     */
    public class drsg{
        public String brf;
        public String txt;
    }

    /**
     * 温馨提示
     */
    public class flu{
        public String brf;
        public String txt;
    }

    /**
     * 运动建议
     */
    public class sport{
        public String brf;
        public String txt;
    }
}
