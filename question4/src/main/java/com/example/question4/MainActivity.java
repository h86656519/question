package com.example.question4;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    HttpHelper httpHelper,httpHelper2;
    private String HTML_URL1 = "http://demo.kidtech.com.tw/files/appexam/appexam1.htm";
    private String HTML_URL2 = "http://demo.kidtech.com.tw/files/appexam/appexam2.htm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternet();

        httpHelper = HttpHelper.getInstance();
        httpHelper2 = HttpHelper.getInstance();
        httpHelper.setPath(HTML_URL1);
        httpHelper2.setPath(HTML_URL2);
        httpHelper.setMethod("GET");
        httpHelper2.setMethod("GET");
        httpHelper.request(); // request() 和 runAnimate() 會同時跑
        httpHelper2.request(); // request() 和 runAnimate() 會同時跑

    }

    public void checkInternet(){
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);//先取得此service
        NetworkInfo networInfo = conManager.getActiveNetworkInfo();  //在取得相關資訊
        if (networInfo == null || !networInfo.isAvailable()) { //判斷是否有網路
            Toast.makeText(MainActivity.this, "沒網路", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "有網路", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        httpHelper.Destroy();
    }
}
