package com.example.weather.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.R;
import com.example.weather.activity.Weather;
import com.example.weather.activity.WeatherMainActivity;
import com.example.weather.db.City;
import com.example.weather.db.County;
import com.example.weather.db.Province;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.ParseLocationJson;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/3.
 */
public class LoadLocationFragment extends Fragment{
    public static final int LEVEL_PROVINCE=0;//表示当前列表数据为省级数据
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;
    private int current_level;//标识现在数据属于哪一个等级

    private Province selectProvince;//选择的省份
    private City selectCity;//所选择的城市
    private County selectCounty;//选择的县市

    private List<Province>province;//存放省份信息的列表
    private List<City>city;//存放地级市信息的列表
    private List<County>county;//存放地级市下的县，市信息的列表

    private Button back_but;
    private TextView title_text;
    private ListView listView;//显示位置信息的ListView
    private View view;
    private ArrayAdapter<String>adapter;//listview的适配器
    private List<String>datalist=new ArrayList<>();//存放省，县，市的数据列表
    private ProgressDialog progressDialog;//加载显示进度加载中对话框
    private SharedPreferences selectShare;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences weatherPrefercnce=getActivity().getSharedPreferences("weather", Context.MODE_PRIVATE);
        String weatherData=weatherPrefercnce.getString("weatherData",null);
        String id=weatherPrefercnce.getString("weather_id",null);
         selectShare=getActivity().getSharedPreferences("seclectLocation", Context.MODE_PRIVATE);
        boolean isSelect=selectShare.getBoolean("isSelect",false);
        if (weatherData!=null&isSelect==false) {
            WeatherMainActivity.actionStartActivity(getContext(),id);
            getActivity().finish();
        }

        view=inflater.inflate(R.layout.loadlocation_frag_layout,container,false);
        init();
        return view;
    }

    /**
     * 初始化控件
     */
    private void init() {
        back_but= (Button) view.findViewById(R.id.loadLocation_back_but);
        title_text= (TextView) view.findViewById(R.id.loadLocation_text);
        listView= (ListView) view.findViewById(R.id.loadLocation_list);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        LoadProvince();
        RegisterListener();
    }

    private void RegisterListener() {
        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_level==LEVEL_CITY){
                    LoadProvince();
                }if(current_level==LEVEL_COUNTY){
                    LoadCitys();
                }
            }
        });
    }

    /**
     * 对listView Item的监听处理
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(current_level==LEVEL_PROVINCE){
                    selectProvince=province.get(position);//拿到所选省份的代码
                    Log.i("此时选择的列表位置:",position+"  "+selectProvince.getProvinceName());
                    LoadCitys();//读取省份下的地级市的数据
                }
                else if (current_level==LEVEL_CITY){
                    selectCity=city.get(position);
                    Log.i("此时选择城市名字:",selectCity.getCityName()+"");
                    Log.i("此时选择的列表位置:",position+"");
                    LoadCountys();
                }
                else if(current_level==LEVEL_COUNTY){
                    selectCounty=county.get(position);
                    Log.i("此时选择的列表位置:",position+"、"+selectCounty.getCountyName());
                    //当当前列表选项为县市的数据并且用户选中了其中某一项时，将所选县市weather_id传递给天气活动
                    String weather_id=selectCounty.getWeather_id();
                    Log.i("weather_id:",weather_id);
                    WeatherMainActivity.actionStartActivity(getContext(),weather_id);
                    SharedPreferences.Editor editor=selectShare.edit();
                    editor.putBoolean("isSelect",false);
                    editor.commit();
                    getActivity().finish();
                }
            }
        });
    }
    /**
     * 查询省级数据,优先从数据库中查询，查不到的话，从服务器上查询
     */
    public  void LoadProvince(){
        title_text.setText("中国");
        back_but.setVisibility(View.GONE);//不能按返回按钮
        province= DataSupport.findAll(Province.class);//先从数据库中寻找有没有省份信息
        Log.i("province列表大小:",province.size()+"");
        if (province.size()>0) {
            datalist.clear();//清空原有的存储数据
            for (Province province1 : province) {
                //取出每个省的名字并加入数据源中
                datalist.add(province1.getProvinceName());
            }
            //通知适配器数据发生改变
            adapter.notifyDataSetChanged();
            //设置默认选中项,默认第一项选中
            listView.setSelection(0);
            current_level = LEVEL_PROVINCE;
        }else{
            String address="http:/guolin.tech/api/china";
            LoadFromServer(address,"province");
        }
    }
    /**
     * 查询地级市级数据,优先从数据库中查询，查不到的话，从服务器上查询
     */
    public void LoadCitys(){
        title_text.setText(selectProvince.getProvinceName());
        back_but.setVisibility(View.VISIBLE);
        //查询特定省份下的所有地级市
        city= DataSupport.where("provinceId=?",String.valueOf(selectProvince.getProvinceCode())).find(City.class);
        Log.i("city列表大小:",city.size()+"");
        if (city.size()>0) {
            datalist.clear();
            for (City city1 : city) {
                //取出每个省的名字并加入数据源中
                datalist.add(city1.getCityName());
            }
            //通知适配器数据发生改变
            adapter.notifyDataSetChanged();
            //设置默认选中项,默认第一项选中
            listView.setSelection(0);
            current_level = LEVEL_CITY;
        }else{
            String address="http:/guolin.tech/api/china/"+selectProvince.getProvinceCode();
            LoadFromServer(address,"city");
        }
    }
    /**
     * 查询县，市数据,优先从数据库中查询，查不到的话，从服务器上查询
     */
    public void LoadCountys(){
        title_text.setText(selectCity.getCityName());
        back_but.setVisibility(View.VISIBLE);
        //查询特定地级市下的所有县市
        county= DataSupport.where("cityId=?",String.valueOf(selectCity.getCityCode())).find(County.class);
        Log.i("county列表大小:",county.size()+"");
        if (county.size()>0) {
            datalist.clear();
            for (County county1 : county) {
                //取出每个县，市的名字并加入数据源中
                Log.i("county列表大小:",county1.getCountyName());
                datalist.add(county1.getCountyName());
            }
            //通知适配器数据发生改变
            adapter.notifyDataSetChanged();
            //设置默认选中项,默认第一项选中
            listView.setSelection(0);
            current_level = LEVEL_COUNTY;
        }else{
            String address="http:/guolin.tech/api/china/"+selectProvince.getProvinceCode()+"/"
                    +selectCity.getCityCode();
            LoadFromServer(address,"county");
        }
    }
    /**
     * 从服务器上查询地方信息
     * @param address
     * @param type
     */
    private void LoadFromServer(String address, final String type) {
        ShowDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CloseDialog();
                        Toast.makeText(getContext(),"加载失败....",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();//拿到服务器返回的数据

                boolean result=false;
                if("province".equals(type)){
                    result= ParseLocationJson.handleProvinceRequest(responseData);
                }else if ("city".equals(type)){
                    result=ParseLocationJson.handleCityRequest(responseData,selectProvince.getProvinceCode());
                }else if ("county".equals(type)){
                    result=ParseLocationJson.handleCountyRequest(responseData,selectCity.getCityCode());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CloseDialog();
                            if("province".equals(type)){
                                LoadProvince();
                            }if("city".equals(type)){
                                LoadCitys();
                            }if ("county".equals(type)){
                                LoadCountys();
                            }
                        }
                    });
                }
            }
        });
    }
    public void ShowDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("玩命加载中.....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    public void CloseDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}
