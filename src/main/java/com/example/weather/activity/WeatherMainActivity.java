package com.example.weather.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weather.Base.BaseActivity;
import com.example.weather.R;

/**
 * 显示天气信息的活动界面
 */
public class WeatherMainActivity extends BaseActivity {
    private TextView weather_now_temperature_text;//显示现在温度文本
    private TextView weather_now_status_text;//显示现在天气状态(风力/天气状况)
    private TextView weather_now_windDirection_text;//显示现在风向
    private TextView weather_now_windLevel_text;//显示现在风力
    private TextView weather_now_humidity_text;//显示现在相对湿度
    private TextView weather_now_feelsendibleTemperature_text;//显示现在体感温度
    private ListView weather_forecast_list;//显示三天预报的信息
    private ImageView weather_suggestion_comfortable_img;//显示体感舒适建议的图片
    private  TextView weather_suggestion_comfortable_brief_text;//显示体感舒适建议的简要描述
    private  TextView weather_suggestion_comfortable_detail_text;//显示体感舒适建议的详细描述
    private ImageView weather_suggestion_dress_img;//显示穿衣指数建议的图片
    private  TextView weather_suggestion_dress_brief_text;//显示穿衣指数建议的简要描述
    private  TextView weather_suggestion_dress_detail_text;//显示穿衣指数建议的详细描述
    private ImageView weather_suggestion_flu_img;//显示温馨提示建议的图片
    private  TextView weather_suggestion_flu_brief_text;//显示温馨提示建议的简要描述
    private  TextView weather_suggestion_flu_detail_text;//显示温馨提示建议的详细描述
    private ImageView weather_suggestion_sport_img;//显示运动建议的图片
    private  TextView weather_suggestion_sport_brief_text;//显示运动建议的简要描述
    private  TextView weather_suggestion_sport_detail_text;//显示运动建议的详细描述

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main_layout);
        Log.i("WeatherMainActivity:",getIntent().getStringExtra("weather_id"));
    }

    /**
     * Back按键的逻辑处理
     */
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
    public static void  actionStartActivity(Context context,String weather_id){
        Intent intent=new Intent(context,WeatherMainActivity.class);
        intent.putExtra("weather_id",weather_id);
        context.startActivity(intent);
    }
}
