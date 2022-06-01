package com.bistu.xudachui.electrochemistry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Calendar;

public class CVActivity extends AppCompatActivity implements View.OnClickListener{
    private SurfaceView sfv_wave;
    private Button bt_start;
    private Button bt_pause;
    private ScrollView sv_receive;
    private Bluetooth_Lab mBtTool;
    private DrawWave mDrawWave;
    private TextView tv_in;
    private Button bt_connect;
    //处理接受数据线程传来的数据的Handler
    private final Handler receiveHandler = new ReceiveHandler(this);
    //数据Y轴最大值
    public static int MaxValue = 1024;
    //数据X轴最大值
    public static int XMaxValue = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvactivity);

        mBtTool = Bluetooth_Lab.init(this,receiveHandler);
        sv_receive = findViewById(R.id.sv_receive);
        sfv_wave = findViewById(R.id.sfv_wave);
        tv_in = findViewById(R.id.in);

        mDrawWave = new DrawWave(this,sfv_wave,MaxValue);

        bt_start = findViewById(R.id.bt_start);
        bt_start.setOnClickListener(this);
        bt_pause = findViewById(R.id.bt_pause);
        bt_pause.setOnClickListener(this);
        bt_connect = findViewById(R.id.bt_blueTooth);
        bt_connect.setOnClickListener(this);

    }
    //页面可见，能交互
    @Override
    protected void onResume() {
        super.onResume();
        mBtTool = Bluetooth_Lab.init(this,receiveHandler);
        mDrawWave.ResetValue(XMaxValue,MaxValue);
        mDrawWave.isDraw = true;

    }
    //页面不能交互
    @Override
    protected void onPause() {
        mDrawWave.isDraw = false;
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_blueTooth:
                Intent intent = new Intent(CVActivity.this,DeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_start:
                mDrawWave.isDraw = false;
                mDrawWave.isDraw = true;
                mDrawWave.startDraw();
                break;
            case R.id.bt_pause:
                mDrawWave.isDraw = false;
                break;
        }

    }


    static class ReceiveHandler extends Handler {
        WeakReference<CVActivity> MyActivity;
        CVActivity mInstance;
        ScrollView sv_receive;
        TextView tv_in;

        public ReceiveHandler(CVActivity activity) {
            MyActivity = new WeakReference<CVActivity>(activity);
            mInstance = MyActivity.get();
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

                if (msg.what == 0) {
                    sv_receive = mInstance.sv_receive;
                    tv_in = mInstance.tv_in;
                    tv_in.setText((String) msg.obj);
                    sv_receive.scrollTo(0,tv_in.getMeasuredHeight()); //跳至数据最后一页
                    //去除最后的\n

                    String[] res = (String[]) msg.obj.toString().split("\r\n");
                    for (String a:res) {
                        String[] s = a.split(",");
                        if(s.length == 2){
                            //字符串转变为数字
                            float resX = Float.parseFloat(s[0]);
                            float resY = Float.parseFloat(s[1]);
                            System.out.println(resX+","+resY);
                            mInstance.mDrawWave.inX = resX;
                            mInstance.mDrawWave.inY = resY;
                        }
                        else {
                            System.out.println("error"+s);
                        }

                    }

            }
        }
    }

}