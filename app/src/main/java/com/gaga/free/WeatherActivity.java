package com.gaga.free;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaga.free.adapter.FutureWeatherAdapter;
import com.gaga.free.bean.DayWeatherBean;
import com.gaga.free.bean.WeatherBean;
import com.gaga.free.utils.NetUtil;
import com.google.gson.Gson;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    public static final String TAG="WeatherActivity";

    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private String[] mCities;

    private TextView tvWeather,tvTem,tvTemLowHigh,tvWin,tvAir;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;
    private FutureWeatherAdapter mWeatherAdapter;

    private DayWeatherBean todayWeather;

    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String weather = (String) msg.obj;
                Log.d("fan", "--主线程收到了天气数据-weather---" + weather);

                Gson gson = new Gson();
                WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
                Log.d("fan", "--解析后的数据-weather---" + weatherBean.toString());

                updateUiOfWeather(weatherBean);

                Toast.makeText(WeatherActivity.this,"更新天气~",Toast.LENGTH_SHORT).show();

            }

        }
    };

    private void updateUiOfWeather(WeatherBean weatherBean) {
        if (weatherBean == null) {
            return;
        }

        List<DayWeatherBean> dayWeathers = weatherBean.getDayWeathers();
        todayWeather = dayWeathers.get(0);
        if (todayWeather == null) {
            return;
        }

        tvTem.setText(todayWeather.getTem());
        tvWeather.setText(todayWeather.getWea()+"("+todayWeather.getDate()+todayWeather.getWeek()+")");
        tvTemLowHigh.setText(todayWeather.getTem2() + "~" + todayWeather.getTem1());
        tvWin.setText(todayWeather.getWin()[0] + todayWeather.getWinSpeed());
        tvAir.setText("空气:" + todayWeather.getAir() + todayWeather.getAirLevel() + "\n" + todayWeather.getAirTips());
        ivWeather.setImageResource(getImgResOfWeather(todayWeather.getWeaImg()));

        dayWeathers.remove(0); // 去掉当天的天气

        // 未来天气的展示
        mWeatherAdapter = new FutureWeatherAdapter(this, dayWeathers);
        rlvFutureWeather.setAdapter(mWeatherAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rlvFutureWeather.setLayoutManager(layoutManager);

    }

    private int getImgResOfWeather(String weaStr) {
        // xue、lei、shachen、wu、bingbao、yun、yu、yin、qing
        int result = 0;
        switch (weaStr) {
            case "qing":
                result = R.drawable.biz_plugin_weather_qing;
                break;
            case "yin":
                result = R.drawable.biz_plugin_weather_yin;
                break;
            case "yu":
                result = R.drawable.biz_plugin_weather_dayu;
                break;
            case "yun":
                result = R.drawable.biz_plugin_weather_duoyun;
                break;
            case "bingbao":
                result = R.drawable.biz_plugin_weather_leizhenyubingbao;
                break;
            case "wu":
                result = R.drawable.biz_plugin_weather_wu;
                break;
            case "shachen":
                result = R.drawable.biz_plugin_weather_shachenbao;
                break;
            case "lei":
                result = R.drawable.biz_plugin_weather_leizhenyu;
                break;
            case "xue":
                result = R.drawable.biz_plugin_weather_daxue;
                break;
            default:
                result = R.drawable.biz_plugin_weather_qing;
                break;
        }

        return result;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weather);

        initView();
        initEvent();
    }

    private void initEvent() {

    }

    private void initView() {

        mSpinner = findViewById(R.id.sp_city);
        mCities = getResources().getStringArray(R.array.cities);
        Log.d(TAG, "initView: mCities"+mCities);
        Log.d(TAG, "initView: mSpinner"+mSpinner);
        mSpAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mCities);

        mSpinner.setAdapter(mSpAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = mCities[position];

                getWeatherOfCity(selectedCity);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvWeather = findViewById(R.id.tv_weather);
        tvAir = findViewById(R.id.tv_air);
        tvTem = findViewById(R.id.tv_tem);
        tvTemLowHigh = findViewById(R.id.tv_tem_low_high);
        tvWin = findViewById(R.id.tv_win);
        ivWeather = findViewById(R.id.iv_weather);
        rlvFutureWeather = findViewById(R.id.rlv_future_weather);

        tvAir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: jump to Tips");
                Intent intent = new Intent(WeatherActivity.this, TipsActivity.class);
//                 将数据传递给tipsActivity
                intent.putExtra("tips", todayWeather);
                startActivity(intent);

            }
        });
    }

    private void getWeatherOfCity(String selectedCity) {
        // 开启子线程，请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 请求网络
                String weatherOfCity = NetUtil.getWeatherOfCity(selectedCity);
                // 使用handler将数据传递给主线程
                Message message = Message.obtain();
                message.what = 0;
                message.obj = weatherOfCity;
                mHandler.sendMessage(message);

            }
        }).start();

    }
}