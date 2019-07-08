package com.example.question4;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;


//待完成
//1.資料整理
//2.同步request
//3.顯示資料
public class MainActivity extends AppCompatActivity {

    private String HTML_URL1 = "http://demo.kidtech.com.tw/files/appexam/appexam1.htm";
    private String HTML_URL2 = "http://demo.kidtech.com.tw/files/appexam/appexam2.htm";

    private GsonParser gsonParser = new GsonParser();
    private ArrayList<String> dataList = new ArrayList<>();
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternet();

        myAdapter = new MyAdapter(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        Thread t1 = new Thread() {
            public void run() {
                try {
                    Message message = new Message();
                    message.what = 0x002;
                    String personData = GetData.getHtmlwithtoken(HTML_URL1); //用token權限，抓下資料
                     ArrayList<Repo> persons = gsonParser.parse(personData.toString()); //方法2 傳過去用Gson做解析
                        Log.i("123", "personData" + personData);
                    message.obj = persons;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
//        MyThreadRunner my1 = new MyThreadRunner(HTML_URL1);
//        Thread t1 = new Thread(my1,"thread 1");
//
//        MyThreadRunner my2 = new MyThreadRunner(HTML_URL2);
//        Thread t2 = new Thread(my2,"thread 2");
//
        Thread t2 = new Thread() {
            public void run() {
                try {
                    Message message = new Message();
                    message.what = 0x002;
                    String personData = GetData.getHtmlwithtoken(HTML_URL2); //用token權限，抓下資料
                    ArrayList<Repo> persons = gsonParser.parse(personData.toString()); //方法2 傳過去用Gson做解析
                    Log.i("123", "personData" + personData);
                    message.obj = persons;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        t2.start();

    }

//    class MyThreadRunner implements Runnable {
//        private String HTML_URL = "";
//
//        public MyThreadRunner(String HTML_URL) {
//            this.HTML_URL = HTML_URL;
//        }
//
//        @Override
//        public void run() {
//                synchronized (HTML_URL) {
//                    try {
//                        Message message = new Message();
//                        message.what = 0x002;
//                        String personData = GetData.getHtmlwithtoken(HTML_URL); //用token權限，抓下資料
//                        ArrayList<Repo> persons = gsonParser.parse(personData.toString()); //方法2 傳過去用Gson做解析
//                        Log.i("123", "personData" + personData);
//                        message.obj = persons;
//                        handler.sendMessage(message);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//        }
//    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x002:
                    ArrayList<Repo> list = (ArrayList<Repo>) msg.obj;
                    myAdapter.setDatelist(list);
                    recyclerView.setAdapter(myAdapter); //必須放在 myAdapter.setNames 之後做
                    Toast.makeText(MainActivity.this, "讀取完畢", Toast.LENGTH_SHORT).show();
//                    Log.i("suvini", "persons.get(0).getId() : " + persons.get(0).getName());
                    break;
                default:
                    break;
            }
        }
    };

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
