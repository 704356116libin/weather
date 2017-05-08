package com.example.weather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.example.weather.R;
import com.example.weather.activity.WeatherMainActivity;
import com.example.weather.json.weather;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.ParseLocationJson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdataWeather extends Service {
    public AutoUpdataWeather() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences weatherPrefercnce = getSharedPreferences("weather", MODE_PRIVATE);
        String weatherData=weatherPrefercnce.getString("weatherData",null);
        weather weather=ParseLocationJson.handleWeatherRequest(weatherData);
        if(weather!=null&&"ok".equals(weather.status)){
            Intent intent=new Intent(this, WeatherMainActivity.class);
            PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
            Notification notification=new NotificationCompat.Builder(this)
                    .setContentTitle(weather.basic.city+":"+weather.now.cond.txt+"  "+weather.now.tmp)
                    .setContentText(weather.aqi.city.qlty)
                    .setSmallIcon(R.drawable.w100)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.w100))
                    .setContentIntent(pi)
                    .build();
            startForeground(1,notification);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        UpdataWeather();
        LoadBiYingImg();
        //创建定时任务
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);//定时服务
        int hour=60*60*60;//设定间隔为一小时
        long triggerAtTime= SystemClock.elapsedRealtime()+hour;
        Intent i=new Intent(this,AutoUpdataWeather.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,i,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 加载每日一图
     */
    private void LoadBiYingImg() {
        String url="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingImg=response.body().string();//拿到图片的url
                SharedPreferences bingImgShare=getSharedPreferences("bingImg",MODE_PRIVATE);
                SharedPreferences.Editor editor=bingImgShare.edit();
                editor.putString("bingImg",bingImg);
                editor.apply();
            }
        });
    }

    /**
     * 自动更新天气
     */
    private void UpdataWeather() {
        final SharedPreferences weatherPrefercnce = getSharedPreferences("weather", MODE_PRIVATE);

        String id = weatherPrefercnce.getString("weather_id", null);
        if (id!=null){
            String url="https://free-api.heweather.com/v5/weather?city=" + id
                    +"faff2b52a8eb46df9017cf9c3e055842";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //将新的天气Json存到SharePercence中
                    String weatherData=response.body().string();
                    weather weather=ParseLocationJson.handleWeatherRequest(weatherData);
                    if(weather!=null&&"ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = weatherPrefercnce.edit();
                        editor.putString("weatherData", weatherData);
                        editor.commit();
                    }
                }
            });

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
