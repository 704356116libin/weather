package com.example.weather.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.weather.Base.BaseActivity;
import com.example.weather.R;

public class Weather extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

    }

    @Override
    public void onBackPressed() {

//        AlertDialog.Builder dialog=new AlertDialog.Builder(Weather.this);
//        dialog.setTitle("亲爱的用户，您好：");
//        dialog.setMessage("您确定要退出程序吗?");
//        dialog.setCancelable(false);
//        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        dialog.show();
        SharedPreferences weatherPrefercnce=Weather.this.getSharedPreferences("weather", Context.MODE_PRIVATE);
        String id=weatherPrefercnce.getString("weather_id",null);
        if (id==null){
            super.onBackPressed();
        }else {
            WeatherMainActivity.actionStartActivity(Weather.this, id);
            finish();
        }
    }
}
