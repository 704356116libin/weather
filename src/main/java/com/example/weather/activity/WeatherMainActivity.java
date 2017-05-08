package com.example.weather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private TextView weather_suggestion_comfortable_brief_text;//显示体感舒适建议的简要描述
    private TextView weather_suggestion_comfortable_detail_text;//显示体感舒适建议的详细描述
    private ImageView weather_suggestion_dress_img;//显示穿衣指数建议的图片
    private TextView weather_suggestion_dress_brief_text;//显示穿衣指数建议的简要描述
    private TextView weather_suggestion_dress_detail_text;//显示穿衣指数建议的详细描述
    private ImageView weather_suggestion_flu_img;//显示温馨提示建议的图片
    private TextView weather_suggestion_flu_brief_text;//显示温馨提示建议的简要描述
    private TextView weather_suggestion_flu_detail_text;//显示温馨提示建议的详细描述
    private ImageView weather_suggestion_sport_img;//显示运动建议的图片
    private TextView weather_suggestion_sport_brief_text;//显示运动建议的简要描述
    private TextView weather_suggestion_sport_detail_text;//显示运动建议的详细描述
    private ProgressDialog progressDialog;//加载显示进度加载中对话框
    //===================================================================
    private ImageView weather_bgImg;//加载必应每日一图
    //============aqi的特别处理，有的城市有aqi，有的没有=================
    private LinearLayout aqi_layout;//显示aqi的父布局
    private TextView aqi_statusText;//显示空气污染情况的文本
    private TextView aqi_dataText;//显示空气指数发布的时间
    private TextView aqi_aqiText;//显示sqi指数的文本
    private TextView aqi_pm25Text;//显示PM2.5的文本
    //===================================================================
    private Button selectLocation_but;
    private FloatingActionButton SeclectLocation;
    private Button addWeather_but;
    private final String KEY = "faff2b52a8eb46df9017cf9c3e055842";//申请的和风天气的API key值
    private String weather_id;//用来接收所选择的城市weather代码
    private weather weather;//服务器返回的天气对象
    private List<forecast> list;//用来存储三天的预报信息
    private SwipeRefreshLayout weatherRefersh;//下拉刷新天气
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main_layout);
        //==========================================
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        weather_id = getIntent().getStringExtra("weather_id");//取得从选择城市页面传递过来的所选城市代码
        init();
        SharedPreferences weatherPrefercnce = getSharedPreferences("weather", MODE_PRIVATE);
        String weatherData = weatherPrefercnce.getString("weatherData", null);
        String id = weatherPrefercnce.getString("weather_id", null);
//        Log.i("SharedPreferences:",weatherData);
//        Log.i("SharedPreferences:",weather_id+"////"+id);
        if (weatherData == null || !weather_id.equals(id)) {
            ShowDialog();
            RequestWeatherInfor();
            CloseDialog();
        } else {
            weather = ParseLocationJson.handleWeatherRequest(weatherData);
            ShowInforOnUi();
        }
    }

    private void RequestWeatherInfor() {
        HttpUtil.sendOkHttpRequest("https://free-api.heweather.com/v5/weather?city=" + weather_id
                + "&key=" + KEY, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                CloseDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String data = response.body().string();
                SharedPreferences weatherPrefercnce = getSharedPreferences("weather", MODE_PRIVATE);
                SharedPreferences.Editor editor = weatherPrefercnce.edit();
                editor.putString("weatherData", data);
                editor.putString("weather_id", weather_id);
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
    }

    /**
     * 初始化各种控件
     */
    private void init() {
        //悬浮按钮
        SeclectLocation= (FloatingActionButton) findViewById(R.id.Seclect_location_but);
        //下拉刷新布局
        weatherRefersh= (SwipeRefreshLayout) findViewById(R.id.weather_refersh);
        //设置下拉刷新按钮的颜色
        weatherRefersh.setColorSchemeResources(R.color.refershColor);
        //必应每日一图的显示
        weather_bgImg= (ImageView) findViewById(R.id.weather_bgImg);
        //各种天气信息的显示
        weather_now_temperature_text = (TextView) findViewById(R.id.weather_now_tp_text);//显示现在温度文本
        weather_now_status_text = (TextView) findViewById(R.id.weather_now_status);//显示现在天气状态(风力/天气状况)
        weather_now_windDirection_text = (TextView) findViewById(R.id.weather_now_windDirection_text);
        weather_now_windLevel_text = (TextView) findViewById(R.id.weather_now_windLever_text);
        weather_now_humidity_text = (TextView) findViewById(R.id.weather_now_hum_text);
        weather_now_feelsendibleTemperature_text = (TextView) findViewById(R.id.weather_now_fl_text);

        //显示未来三天天气预报的列表
        weather_forecast_list = (RecyclerView) findViewById(R.id.weather_forecast_list);

        //显示建议信息的图片
        weather_suggestion_comfortable_img = (ImageView) findViewById(R.id.weather_suggestion_comf_img);
        weather_suggestion_dress_img = (ImageView) findViewById(R.id.weather_suggestion_drsg_img);
        weather_suggestion_flu_img = (ImageView) findViewById(R.id.weather_suggestion_flu_img);
        weather_suggestion_sport_img = (ImageView) findViewById(R.id.weather_suggestion_sport_img);
        //显示建议信息的文本显示
        weather_suggestion_comfortable_brief_text = (TextView) findViewById(R.id.weather_suggestion_comf_brf_text);
        weather_suggestion_comfortable_detail_text = (TextView) findViewById(R.id.weather_suggestion_comf_txt_text);
        weather_suggestion_dress_brief_text = (TextView) findViewById(R.id.weather_suggestion_drsg_brf_text);
        weather_suggestion_dress_detail_text = (TextView) findViewById(R.id.weather_suggestion_drsg_txt_text);
        weather_suggestion_flu_brief_text = (TextView) findViewById(R.id.weather_suggestion_flu_brf_text);
        weather_suggestion_flu_detail_text = (TextView) findViewById(R.id.weather_suggestion_flu_txt_text);
        weather_suggestion_sport_brief_text = (TextView) findViewById(R.id.weather_suggestion_sport_brf_text);
        weather_suggestion_sport_detail_text = (TextView) findViewById(R.id.weather_suggestion_sport_txt_text);
        //空气指数aqi的控件绑定
        aqi_layout= (LinearLayout) findViewById(R.id.aqi_layout);
        aqi_statusText= (TextView) findViewById(R.id.aqi_statusText);
        aqi_dataText= (TextView) findViewById(R.id.aqi_dataText);
        aqi_aqiText= (TextView) findViewById(R.id.aqi_aqiText);
        aqi_pm25Text= (TextView) findViewById(R.id.aqi_pm25Text);
        RegisterListener();
    }

    /**
     * 控件监听的处理
     */
    private void RegisterListener() {
        //悬浮按钮的监听
        SeclectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences selectShare = WeatherMainActivity.this.getSharedPreferences("seclectLocation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = selectShare.edit();
                editor.putBoolean("isSelect", true);
                editor.commit();
                Intent intent = new Intent(WeatherMainActivity.this, Weather.class);
                WeatherMainActivity.this.startActivity(intent);
                finish();
            }
        });
        //下拉刷新的监听
        weatherRefersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                RequestWeatherInfor();
                weatherRefersh.setRefreshing(false);
            }
        });
    }

    /**
     * Back按键的逻辑处理
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(WeatherMainActivity.this);
        dialog.setTitle("亲爱的用户，您好：");
        dialog.setMessage("您确定要退出程序吗?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    /**
     * 最佳启动活动的方法
     *
     * @param context
     * @param weather_id
     */
    public static void actionStartActivity(Context context, String weather_id) {
        Intent intent = new Intent(context, WeatherMainActivity.class);
        intent.putExtra("weather_id", weather_id);
        context.startActivity(intent);
    }


    private void ShowInforOnUi() {
        if (weather.aqi==null){
            aqi_layout.setVisibility(View.GONE);
        }else{
            aqi_layout.setVisibility(View.VISIBLE);
            aqi_statusText.setText(weather.aqi.city.qlty);
            aqi_aqiText.setText(weather.aqi.city.aqi);
            aqi_pm25Text.setText(weather.aqi.city.pm25);
            String data=weather.basic.update.loc;
            String str[]=data.split(" ",2);//以空格将服务器更新日期截取为两段
            aqi_dataText.setText(str[1]);
        }
        //加载必应每日一图
        SharedPreferences bingImgShare=getSharedPreferences("bingImg",MODE_PRIVATE);
        String bingImg=bingImgShare.getString("bingImg",null);
        if (bingImg==null){
            LoadBiYingImg();
        }else {
            Glide.with(WeatherMainActivity.this).load(bingImg).into(weather_bgImg);
        }
        weather_now_temperature_text.setText(weather.now.tmp + "°");//设置当前温度
        weather_now_status_text.setText(weather.basic.city + "/" + weather.now.cond.txt);//显示现在天气状态(风力/天气状况)
        weather_now_windDirection_text.setText(weather.now.wind.dir);//显示现在风向
        weather_now_windLevel_text.setText(weather.now.wind.sc + "级");//显示现在风力
        weather_now_humidity_text.setText(weather.now.hum + "%");//显示现在相对湿度
        weather_now_feelsendibleTemperature_text.setText(weather.now.fl + "°");//显示现在体感温度
        weather_suggestion_comfortable_brief_text.setText(weather.suggestion.comf.brf);
        weather_suggestion_comfortable_detail_text.setText(weather.suggestion.comf.txt);
        weather_suggestion_dress_brief_text.setText(weather.suggestion.drsg.brf);
        weather_suggestion_dress_detail_text.setText(weather.suggestion.drsg.txt);
        weather_suggestion_flu_brief_text.setText(weather.suggestion.flu.brf);
        weather_suggestion_flu_detail_text.setText(weather.suggestion.flu.txt);
        weather_suggestion_sport_brief_text.setText(weather.suggestion.sport.brf);
        weather_suggestion_sport_detail_text.setText(weather.suggestion.sport.txt);
        //添加未来三天天气预报的信息
        list = weather.daily_forecast;
        LinearLayoutManager manager = new LinearLayoutManager(WeatherMainActivity.this);
        ForecastRecycleAdapter adapter = new ForecastRecycleAdapter(list);
        weather_forecast_list.setLayoutManager(manager);
        weather_forecast_list.setAdapter(adapter);
        Log.i("daily_forecast:", list.size() + "");
    }

    class ForecastRecycleAdapter extends RecyclerView.Adapter<ForecastRecycleAdapter.ViewHolder> {
        List<forecast> list;

        public ForecastRecycleAdapter(List<forecast> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            forecast forecast = list.get(position);
            holder.data.setText(forecast.date);
            holder.status_img.setImageResource(R.drawable.w100);
            holder.status.setText(forecast.cond.txt_d);
            holder.tp.setText(forecast.tmp.max + "/" + forecast.tmp.min + "°");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView data;//显示预报日期
            TextView status;//显示天气状况（白天）
            TextView tp;//显示最高温最低温
            ImageView status_img;

            public ViewHolder(View itemView) {
                super(itemView);
                data = (TextView) itemView.findViewById(R.id.weather_forecastitem_data);
                status = (TextView) itemView.findViewById(R.id.weather_forecastitem_status);
                tp = (TextView) itemView.findViewById(R.id.weather_forecastitem_tp);
                status_img = (ImageView) itemView.findViewById(R.id.weather_forecastitem_status_img);
            }
        }
    }

    /**
     * 加载必应每日一图
     */
    public void LoadBiYingImg(){
        String imagUrl="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(imagUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingImg=response.body().string();//拿到图片的url
                SharedPreferences bingImgShare=getSharedPreferences("bingImg",MODE_PRIVATE);
                SharedPreferences.Editor editor=bingImgShare.edit();
                editor.putString("bingImg",bingImg);
                editor.apply();
                //更新图片
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherMainActivity.this).load(bingImg).into(weather_bgImg);
                    }
                });
            }
        });
    }
    public void ShowDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(WeatherMainActivity.this);
            progressDialog.setMessage("玩命加载中.....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void CloseDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
