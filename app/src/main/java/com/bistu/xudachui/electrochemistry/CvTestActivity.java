package com.bistu.xudachui.electrochemistry;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/*
  利用MPAndroid没做出来，问题在于我要在一个X点取多Y值，经多多次实验，不太行，可能是Entry的问题
 */
public class CvTestActivity extends AppCompatActivity {
    private Bluetooth_Lab mBtTool;
    private Button bt_Start;
    private TextView tv_rec;
    //画图
    private LineChart lineChart ;
    private static Thread chartThread;//画图线程
    private int count = 0;

    //数据

    List<String> xDataList = new ArrayList<>();// x轴数据源
    List<Entry> yDataList = new ArrayList<>();// y轴数据数据源
    //处理接受数据线程传来的数据的Handler
    private final Handler receiveHandler = new ReceiveHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_test);

        mBtTool = Bluetooth_Lab.init(this,receiveHandler);
        tv_rec = findViewById(R.id.tv_receive);
        lineChart = findViewById(R.id.lineChart);
        bt_Start = findViewById(R.id.beginTest);





        bt_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChart();
            }
        });

        //后续
        /**
         * 双向链表
         */
        int k=0;
        float i=0;
        while (k<=23){
            int range = 100;
            xDataList.add(i + " ");
            float value = (float) (Math.random() * range) + 3;
            yDataList.add(new Entry(value, (int)i));
            i= (float) (i+0.73);
            k++;}
        ChartUtil.showChart(CvTestActivity.this, lineChart, xDataList, yDataList, "随机图", "C/V","I/E");




    }


////给上面的X、Y轴数据源做假数据测试
//        for (int i = 0; i < 24; i++) {
//
//            int range = 100;
//            // x轴显示的数据
//            xDataList.add(i + " ");
//            //y轴生成float类型的随机数
//            float value = (float) (Math.random() * range) + 3;
//            yDataList.add(new Entry(value, i));
//        }
//显示图表,参数（ 上下文，图表对象， X轴数据，Y轴数据，图表标题，曲线图例名称，坐标点击弹出提示框中数字单位）
//        ChartUtil.showChart(this, lineChart, xDataList, yDataList, "随机图", "C/V","I/E");



    private void startChart() {
        chartThread = new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 双向链表
                 */
                int k=0;
                float i=0;
                while (k<=23){
                    int range = 100;
                    xDataList.add(i + " ");
                    float value = (float) (Math.random() * range) + 3;
                    yDataList.add(new Entry(value, (int)i));
                    i= (float) (i+0.73);
                    k++;}
                ChartUtil.showChart(CvTestActivity.this, lineChart, xDataList, yDataList, "随机图", "C/V","I/E");
                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        chartThread.start();
    }

    static class ReceiveHandler extends Handler {
        WeakReference<CvTestActivity> MyActivity;
        CvTestActivity mInstance;
        TextView tv_rec;


        public ReceiveHandler(CvTestActivity activity) {
            MyActivity = new WeakReference<CvTestActivity>(activity);
            mInstance = MyActivity.get();
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    mInstance.count += 1;
                    //将字符串以逗号分隔
                    String[] res = (String[]) msg.obj.toString().split(",");
                    //字符串转变为数字
                    float resX = Float.parseFloat(res[0]);
                    float resY = Float.parseFloat(res[1]);

                    mInstance.xDataList.add(resX+"");
                    mInstance.yDataList.add(new Entry(resY, (int)resX));

                    tv_rec = mInstance.tv_rec;
                    tv_rec.setText((String) msg.obj);


                    }


            }catch (Exception e){

            }

        }

    }


}