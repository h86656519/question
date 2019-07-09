package com.example.question4;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//遇到的問題
//No adapter attached; skipping layout

public class MainActivity extends AppCompatActivity {
    String[] hostList = {"http://demo.kidtech.com.tw/files/appexam/appexam1.htm", "http://demo.kidtech.com.tw/files/appexam/appexam2.htm"};
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternet(); //確認網路

        ExecutorService executor = Executors.newSingleThreadExecutor(); //用ExecutorService 確保同步執行
        myAdapter = new MyAdapter(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);

        for (int i = 0; i < hostList.length; i++) {
            String url = hostList[i];
            GetData worker = new GetData(url);
            worker.setListener(new GetData.RunnableListener() {
                @Override
                public void onResult(ArrayList<Repo> person) { //將資料接回來，set到recyclerView裡
                    myAdapter.addList(person);
                    myAdapter.notifyDataSetChanged();
                }
            });
            executor.execute(worker);
        }
        executor.shutdown();
    }


    public void checkInternet() {
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

    }

}
