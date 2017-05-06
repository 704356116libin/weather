package com.example.weather.util;

import android.text.TextUtils;

import com.example.weather.db.City;
import com.example.weather.db.County;
import com.example.weather.db.Province;
import com.example.weather.json.weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**该类用来解析位置数据
 * Created by 彬~ on 2017/5/3.
 */

public class ParseLocationJson {
    /**
     * 解析服务器返回的省级数据
     */
    public static boolean handleProvinceRequest(String responce){
        if(!TextUtils.isEmpty(responce)){
            try {
                JSONArray allProvice=new JSONArray(responce);//以服务器返回的数据拿到一个JSONArray
                for (int i=0;i<allProvice.length();i++){
                    JSONObject provinceObject=allProvice.getJSONObject(i);//依次取出一个省份对象
                    Province  province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();//保存到Provinces数据库中
                }
                return  true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;

    }
    /**
     * 解析服务器返回的市级数据
     */
    public static boolean handleCityRequest(String responce,int provinceId){
        if(!TextUtils.isEmpty(responce)){
            try {
                JSONArray allCitys=new JSONArray(responce);
                for (int i=0;i<allCitys.length();i++){
                    JSONObject cityObject=allCitys.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return  true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;

    }
    /**
     * 解析服务器返回的市级下面的县市数据
     */
    public static boolean handleCountyRequest(String responce,int cityId){
        if(!TextUtils.isEmpty(responce)){
            try {
                JSONArray allCounties=new JSONArray(responce);
                for (int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeather_id(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return  true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }

}
