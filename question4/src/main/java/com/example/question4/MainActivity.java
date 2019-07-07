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

import java.lang.reflect.Array;
import java.util.ArrayList;


//待完成
//1.資料整理
//2.同步request
//3.顯示資料
public class MainActivity extends AppCompatActivity {

//    private String HTML_URL1 = "http://demo.kidtech.com.tw/files/appexam/appexam1.htm";
    private String HTML_URL1 = "http://demo.kidtech.com.tw/files/appexam/appexam2.htm";
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

        new Thread() {
            public void run() {
                try {
                    Message message = new Message();
                    message.what = 0x002;

                   String personData = GetData.getHtmlwithtoken(HTML_URL1); //用token權限，抓下資料
//                    ArrayList<Repo> persons = gsonParser.parse(personData.toString()); //方法2 傳過去用Gson做解析
                //    Log.i("123", "personData" + personData);
                    Repo repo = new Repo();
//                    repo.setID(personData.substring(14,16));
//                    repo.setName(personData.substring(31,41));
//                    repo.setAttack(personData.substring(57,59));
//                    repo.setDefense(personData.substring(75,78));

                    Log.i("123", " personData.length() : " + personData.length());
//                    Log.i("123", "id : " + personData.substring(14,16));//01
//                    Log.i("123", "Name : " + personData.substring(31,41));//01
//                    Log.i("123", "Attack : " + personData.substring(57,59));//01
//                    Log.i("123", "Defense : " + personData.substring(75,78));//01

                    Log.i("123", "id : " + personData.substring(14,16));//02
                    Log.i("123", "Name : " + personData.substring(31,35));//02
                    Log.i("123", "Attack : " + personData.substring(51,53));//02
                    Log.i("123", "Defense : " + personData.substring(69,72));//02

//                    message.obj = repo;
//                    message2.obj = GetData.getHtml(HTML_URL1); //second things
                    handler.sendMessage(message);
//                    handler.sendMessage(message2); //second things
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x002:
//                    ArrayList<Repo> list = (ArrayList<Repo>) msg.obj;

//                    myAdapter.setDatelist(list);
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
