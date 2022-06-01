package com.bistu.xudachui.electrochemistry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_serial;//串口助手
    private Button bt_connect;//蓝牙连接
    private Button bt_CVTest;//循环伏安
    private Button bt_CATest;//计时电流
    private Button bt_CV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_serial = findViewById(R.id.serial);
        bt_serial.setOnClickListener(this);
        bt_connect = findViewById(R.id.bt_Connect);
        bt_connect.setOnClickListener(this);
        bt_CVTest = findViewById(R.id.CV_Test);
        bt_CVTest.setOnClickListener(this);
        bt_CATest = findViewById(R.id.CA_Test);
        bt_CATest.setOnClickListener(this);
        bt_CV = findViewById(R.id.bt_CV);
        bt_CV.setOnClickListener(this);
    }


    //Button响应
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_Connect:
                Intent intent = new Intent(MainActivity.this,DeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.CV_Test:
                Intent intentCV = new Intent(MainActivity.this,CvTestActivity.class);
                startActivity(intentCV);
                Toast.makeText(MainActivity.this,"循环伏安测试",Toast.LENGTH_SHORT).show();
                break;
            case R.id.CA_Test:
                Toast.makeText(MainActivity.this,"还没制作哦！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.serial:
                Toast.makeText(MainActivity.this,"快了！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_CV:
                Intent intent1 = new Intent(MainActivity.this, CVActivity.class);
                startActivity(intent1);
                break;

        }




    }



}