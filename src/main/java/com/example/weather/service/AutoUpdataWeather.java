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

/**
 * 该服务的主要功能：实现后台自动更新天气的信息
 */
public class AutoUpdataWeather extends Service {
    public AutoUpdataWeather() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //
        SharedPreferences weatherPrefercnce = getSharedPreferences("weather", MODE_PRIVATE);
        String weatherData=weatherPrefercnce.getString("weatherData",null);
        weather weather=ParseLocationJson.handleWeatherRequest(weatherData);
        //若服务器接口状态为ok且确实有数据的时候
        if(weather!=null&&"ok".equals(weather.status)){
            Intent intent=new Intent(AutoUpdataWeather.this, WeatherMainActivity.class);
            //当点击这个通知，设置意图
            PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
            //创建一个通知
            Notification notification=new NotificationCompat.Builder(this)
                    .setContentTitle(weather.basic.city+"/"+weather.now.cond.txt+"  "+weather.now.tmp+"°")
                    .setContentText("空气质量："+weather.aqi.city.qlty)
                    .setSmallIcon(R.drawable.weather_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.weather_icon))
                    .setContentIntent(pi)
                    .build();
            startForeground(1,notification);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        UpdataWeather();
        //创建定时任务，拿到AlarmManager
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);//定时服务
        int hour=60;//设定间隔为一小时
        long triggerAtTime= SystemClock.elapsedRealtime()+hour;
        //间隔一定时间重新唤醒该服务
        Intent i=new Intent(this,AutoUpdataWeather.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,i,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * 自动更新天气
     */
    private void UpdataWeather() {
        //final常量只会赋值一次
        //拿到存储天气JSON数据的SharedPreferences
        final SharedPreferences weatherPrefercnce = getSharedPreferences("weather", MODE_PRIVATE);
        //取出之前读取城市的 天气代码
        String id = weatherPrefercnce.getString("weather_id", null);
        if (id!=null){
            //若id不为空则重新请求服务器，刷新SharedPreferences中的天气
            //https://free-api.heweather.com/v5/weather?city=yourcity&key=faff2b52a8eb46df9017cf9c3e055842
            String url="https://free-api.heweather.com/v5/weather?city=" + id
                    +"faff2b52a8eb46df9017cf9c3e055842";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //将新的天气Json存到SharePercence中
                    String weatherData=response.body().string();//将weatherData转化为字符串信息
                    weather weather=ParseLocationJson.handleWeatherRequest(weatherData);//调用解析天气Json数据的方法
                    //若服务器返回天气Json返回值状态为ok的时候将weatherData存储到SharedPreferences中
                    if("ok".equals(weather.status)) {
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
