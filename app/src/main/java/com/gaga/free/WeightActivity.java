package com.gaga.free;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeightActivity extends AppCompatActivity {

    private static final String TAG = "*****BMI*****";
    Date date;
    TextView time, textView;
    EditText et1, et2, output;
    Button rb1, rb2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        Log.i("11111", "onCreate:start running!");
        Log.i(TAG, "onCreate:");

        //获取时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        date = new Date(System.currentTimeMillis());
        time = findViewById(R.id.time);
        final String str = simpleDateFormat.format(date);
        time.setText("今天是" + str);

        //获取属性
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        et1 = findViewById(R.id.edit1);
        et2 = findViewById(R.id.edit2);
        output = findViewById(R.id.out);
    }
    public void myclick(View v){
            Log.i(TAG,"OnClick:AAAAAAAA");
            Double h = Double.parseDouble(et1.getText().toString());
            Double w = Double.parseDouble(et2.getText().toString());
            if (h <= 0 || w <= 0) {
                output.setText("ERROR!");
                return;
            }
            h = h / 100;
            double res = w / (h * h);
            String str = "******中国标准******\nBMI: ";
            String str1 = String.format(Locale.CHINA, "%.2f", res);
            str = str + str1;
            str += "\n您的身体：";
            if(res < 18.5)
                str += "偏瘦";
            else if(res < 24)
                str += "健康";
            else if(res < 28)
                str += "偏胖";
            else
                str += "肥胖";
            output.setText(str);
    }
}
