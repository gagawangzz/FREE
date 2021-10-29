package com.gaga.free;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WeightActivity extends AppCompatActivity {

    private static final String TAG = "*****BMI*****";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        Log.i("11111","onCreate:start running!");
        Log.i(TAG,"onCreate:");

//        rb1 = findViewById()
    }

    public void myclick(View V){
        Log.i(TAG,"OnClick:AAAAAA");
//        double

    }
}
