package com.example.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements CalendarView.OnDateSelectedListener, CalendarView.OnDateChangeListener {

    TextView mTextMonthDay;
   RecyclerView mRecyclerView;
    TextView mTextYear;
    TextView mTextLunar;
    TextView mTv2;
    TextView mTv3;
    TextView mTv4;
    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getDataFromNet();
//        getDatasync();
        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        mTv2 = (TextView)findViewById(R.id.textView2) ;
        mTv3 = (TextView)findViewById(R.id.textView3) ;
        mTv4 = (TextView)findViewById(R.id.textView4) ;
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.showSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });


        mCalendarView.setOnDateChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTv3.setText("24℃");
        mTv4.setText("小雨");
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        List<Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        Log.d("TAG","ok");
        schemes.add(getSchemeCalendar(year, month, 3, 0xFF40db25));
        schemes.add(getSchemeCalendar(year, month, 6, 0xFFe69138));
        schemes.add(getSchemeCalendar(year, month, 9, 0xFFdf1356));
        schemes.add(getSchemeCalendar(year, month, 13, 0xFFedc56d));
        schemes.add(getSchemeCalendar(year, month, 15, 0xFFaacc44));
        schemes.add(getSchemeCalendar(year, month, 18, 0xFFbc13f0));
        schemes.add(getSchemeCalendar(year, month, 25, 0xFF13acf0));
        mCalendarView.setSchemeDate(schemes);
        initData();

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        return calendar;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateChange(Calendar calendar) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onDateSelected(Calendar calendar) {
        onDateChange(calendar);
    }


    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }
     //http://v.juhe.cn/laohuangli/d?date=2014-09-11&key=c987c08bb98d37f4746f32ae197a3892
 private void initData(){
        String url = "http://apis.juhe.cn/simpleWeather/query?city=%E6%88%90%E9%83%BD&key=017128a6fc5c11c75608680f362168ad";
     OkHttpUtils.get()
             .url(url).
             build().execute(new StringCallback() {
         @Override
         public void onError( Call call, Exception e, int id ) {
             String urlTypeName="";
             try {
                  urlTypeName =URLEncoder.encode("成都","utf-8");
             } catch (UnsupportedEncodingException e1) {
                 e1.printStackTrace();
             }
             Log.d("TAG",urlTypeName);

         }
         @Override
         public void onResponse( String response, int id ) {
             Log.d("TAG","ok2"+response);
                 processData(response);
         }
     });
 }
//     private void getDataFromNet() {
////         final String URL = "http://apis.juhe.cn/simpleWeather/query?city=%E6%88%90%E9%83%BD&key=017128a6fc5c11c75608680f362168ad";
////         new Thread(new Runnable() {
////             @Override
////             public void run() {
////                 // 1 创建okhttpclient对象
////                 OkHttpClient okHttpClient = new OkHttpClient();
////                 RequestBody requestBody = new FormBody.Builder()
////                         .add("Number", "1")
////                         .build();
////                 // 2 创建请求方式
////                 Request request = new Request.Builder()
////                         .url(URL)
////                         .post(requestBody)
////                         .build();
////                 Response response = null;
////                 // 3 执行请求操作
////                 try {
////                     Log.d("TAG","欧克1");
////                     response = okHttpClient.newCall(request).execute();
////                     Log.d("TAG","欧克");
////                     //判断请求是否成功
////                     if (response.isSuccessful()) {
////                         //打印服务端返回结果
////                         final String res = response.body().string();
////                         //请求成功则解析
////                         Log.d("TAG","成功");
////                         processData(res);
////                     }
////                 } catch (IOException e) {
////                     e.printStackTrace();
////                 }
////             }
////
////         }).start();
////     }
//     public void getDatasync(){
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                 try {
//                     OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
//                     Request request = new Request.Builder()
//                             .url("http://www.baidu.com")//请求接口。如果需要传参拼接到接口后面。
//                             .build();//创建Request 对象
//                     Response response = null;
//                     response = client.newCall(request).execute();//得到Response 对象
//                     if (response.isSuccessful()) {
//                         Log.d("kwwl","response.code()=="+response.code());
//                         Log.d("kwwl","response.message()=="+response.message());
//                         Log.d("kwwl","res=="+response.body().string());
//                         //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
//                     }
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//             }
//         }).start();
//     }

private void processData(String json){
        JsonBean jsonBean = JSON.parseObject(json,JsonBean.class);
        String wendu = jsonBean.getResult().getRealtime().getTemperature();
        String xinxi = jsonBean.getResult().getRealtime().getInfo();
        mTv3.setText(wendu);
        mTv4.setText(xinxi);
}
}
