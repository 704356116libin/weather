package com.example.weather.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weather.Base.BaseActivity;
import com.example.weather.R;
import com.example.weather.json.forecast;
import com.example.weather.json.weather;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.ParseLocationJson;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private RecyclerView weather_forecast_list;//显示三天预报的信息
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

    private  final String KEY="faff2b52a8eb46df9017cf9c3e055842";//申请的和风天气的API key值
    private  String weather_id;//用来接收所选择的城市weather代码
    private  weather weather;//服务器返回的天气对象
    private List<forecast>list;//用来存储三天的预报信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main_layout);
        weather_id=getIntent().getStringExtra("weather_id");
        init();
        SharedPreferences weatherPrefercnce=getSharedPreferences("weather",MODE_PRIVATE);
        String weatherData=weatherPrefercnce.getString("weatherData",null);
        Log.i("SharedPreferences:",weatherData);
        if (weatherData==null) {
            HttpUtil.sendOkHttpRequest("https://free-api.heweather.com/v5/weather?city=" + weather_id
                    + "&key=" + KEY, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String data = response.body().string();
                    SharedPreferences weatherPrefercnce=getSharedPreferences("weather",MODE_PRIVATE);
                    SharedPreferences.Editor editor=weatherPrefercnce.edit();
                    editor.putString("weatherData",data);
                    editor.commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weather = ParseLocationJson.handleWeatherRequest(data);
                            ShowInforOnUi();
                        }
                    });

                }
            });
        }else {
            weather = ParseLocationJson.handleWeatherRequest(weatherData);
            ShowInforOnUi();
        }
        Log.i("WeatherMainActivity:",getIntent().getStringExtra("weather_id"));
    }

    /**
     * 初始化各种控件
     */
    private void init() {
        weather_now_temperature_text= (TextView) findViewById(R.id.weather_now_tp_text);//显示现在温度文本
        weather_now_status_text= (TextView) findViewById(R.id.weather_now_status);//显示现在天气状态(风力/天气状况)
        weather_now_windDirection_text= (TextView) findViewById(R.id.weather_now_windDirection_text);
        weather_now_windLevel_text= (TextView) findViewById(R.id.weather_now_windLever_text);
        weather_now_humidity_text= (TextView) findViewById(R.id.weather_now_hum_text);
        weather_now_feelsendibleTemperature_text= (TextView) findViewById(R.id.weather_now_fl_text);
        //显示未来三天天气预报的列表
        LinearLayoutManager manager=new LinearLayoutManager(WeatherMainActivity.this);
        ForecastRecycleAdapter adapter=new ForecastRecycleAdapter(list);
        weather_forecast_list= (RecyclerView) findViewById(R.id.weather_forecast_list);
        //显示建议信息的图片
        weather_suggestion_comfortable_img= (ImageView) findViewById(R.id.weather_suggestion_comf_img);
        weather_suggestion_dress_img= (ImageView) findViewById(R.id.weather_suggestion_drsg_img);
        weather_suggestion_flu_img= (ImageView) findViewById(R.id.weather_suggestion_flu_img);
        weather_suggestion_sport_img= (ImageView) findViewById(R.id.weather_suggestion_sport_img);
        //显示建议信息的文本显示
        weather_suggestion_comfortable_brief_text= (TextView) findViewById(R.id.weather_suggestion_comf_brf_text);
        weather_suggestion_comfortable_detail_text= (TextView) findViewById(R.id.weather_suggestion_comf_txt_text);
        weather_suggestion_dress_brief_text= (TextView) findViewById(R.id.weather_suggestion_drsg_brf_text);
        weather_suggestion_dress_detail_text= (TextView) findViewById(R.id.weather_suggestion_drsg_txt_text);
        weather_suggestion_flu_brief_text= (TextView) findViewById(R.id.weather_suggestion_flu_brf_text);
        weather_suggestion_flu_detail_text= (TextView) findViewById(R.id.weather_suggestion_flu_txt_text);
        weather_suggestion_sport_brief_text= (TextView) findViewById(R.id.weather_suggestion_sport_brf_text);
        weather_suggestion_sport_detail_text= (TextView) findViewById(R.id.weather_suggestion_sport_txt_text);
//        list=weather.forecastList;
//        Log.i("11111",list.size()+"");
    }
    /**
     * Back按键的逻辑处理
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 最佳启动活动的方法
     * @param context
     * @param weather_id
     */
    public static void  actionStartActivity(Context context,String weather_id){
        Intent intent=new Intent(context,WeatherMainActivity.class);
        intent.putExtra("weather_id",weather_id);
        context.startActivity(intent);
    }



    private void ShowInforOnUi() {
        weather_now_temperature_text.setText(weather.now.tmp+"°");//设置当前温度
        weather_now_status_text.setText(weather.now.cond.txt);//显示现在天气状态(风力/天气状况)
        weather_now_windDirection_text.setText(weather.now.wind.dir);//显示现在风向
        weather_now_windLevel_text.setText(weather.now.wind.sc);//显示现在风力
        weather_now_humidity_text.setText(weather.now.hum+"%");//显示现在相对湿度
        weather_now_feelsendibleTemperature_text.setText(weather.now.fl+"°");//显示现在体感温度
        weather_suggestion_comfortable_brief_text.setText(weather.suggestion.comf.brf);
        weather_suggestion_comfortable_detail_text.setText(weather.suggestion.comf.txt);
        weather_suggestion_dress_brief_text.setText(weather.suggestion.drsg.brf);
        weather_suggestion_dress_detail_text.setText(weather.suggestion.drsg.txt);
        weather_suggestion_flu_brief_text.setText(weather.suggestion.flu.brf);
        weather_suggestion_flu_detail_text.setText(weather.suggestion.flu.txt);
        weather_suggestion_sport_brief_text.setText(weather.suggestion.sport.brf);
        weather_suggestion_sport_detail_text.setText(weather.suggestion.sport.txt);
        //添加未来三天天气预报的信息
    }
    class ForecastRecycleAdapter extends RecyclerView.Adapter<ForecastRecycleAdapter.ViewHolder>{
        List<forecast>list;

        public ForecastRecycleAdapter(List<forecast> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
