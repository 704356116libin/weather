package com.example.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**来访问服务器获取到全国歌市的数据
 * Created by 彬~ on 2017/5/3.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();//获取OkHttpClient实例
        Request request=new Request.Builder()
                        .url(address)
                        .build();
        client.newCall(request).enqueue(callback);

    }
}
