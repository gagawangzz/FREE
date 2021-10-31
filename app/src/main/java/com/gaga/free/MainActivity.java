package com.gaga.free;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gaga.free.calen.CustomCalendar;
import com.gaga.free.calen.dao.EventData;
import com.gaga.free.calen.dao.dataAboutDate;
import com.gaga.free.calen.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private CustomCalendar customCalendar;
    public static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCalendar = (CustomCalendar) findViewById(R.id.customCalendar);


        String[] arr = {"2021-10-10", "2021-10-26", "2021-09-05", "2021-11-02", "2021-11-26"};
//        arr = getParentActivityIntent().getStringArrayExtra();
        for (int i = 0; i < 5; i++) {
            Log.d(TAG, "onCreate: watching ");
            int eventCount = 1;
            customCalendar.addAnEvent(arr[i], eventCount, getEventDataList(eventCount));
        }
    }

    public ArrayList<EventData> getEventDataList(int count) {
        ArrayList<EventData> eventDataList = new ArrayList();

        for (int i = 0; i < count; i++) {
            EventData dateData = new EventData();
            ArrayList<dataAboutDate> dataAboutDates = new ArrayList();

            dateData.setSection(CalendarUtils.getNAMES()[new Random().nextInt(CalendarUtils.getNAMES().length)]);
            dataAboutDate dataAboutDate = new dataAboutDate();

            int index = new Random().nextInt(CalendarUtils.getEVENTS().length);

            dataAboutDate.setTitle(CalendarUtils.getEVENTS()[index]);
            dataAboutDate.setSubject(CalendarUtils.getEventsDescription()[index]);
            dataAboutDates.add(dataAboutDate);

            dateData.setData(dataAboutDates);
            eventDataList.add(dateData);
        }

        return eventDataList;
    }

    public void myclicka(View v){
        //添加点击响应事件
        Intent intent =new Intent(MainActivity.this, com.gaga.free.WeatherActivity.class);
        //启动
        startActivity(intent);
    }

    public void myclickb(View v){
        //添加点击响应事件
        Intent intent =new Intent(MainActivity.this,WeightActivity.class);
        //启动
        startActivity(intent);
    }
}