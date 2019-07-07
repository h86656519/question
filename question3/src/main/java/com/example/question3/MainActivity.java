package com.example.question3;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String HTML_URL = "http://news.ltn.com.tw/rss/focus.xml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread() {
            public void run() {
                try {
                    Message message = new Message();
                    message.what = 0x002;
                    String personData = GetData.getHtmlwithtoken(HTML_URL); //用token權限，抓下資料
                  //  ArrayList<Repo> persons = gsonParser.parse(personData.toString()); //方法2 傳過去用Gson做解析

                  //  message.obj = persons;
                    handler.sendMessage(message);
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
                    ArrayList<Repo> persons = (ArrayList<Repo>) msg.obj;
                    // id.setText((String)msg.obj);
                    String nameString = "";
//                    for (int i = 0; i < persons.size(); i++) {
//                        nameString += persons.get(i).getName() + ", ";
//                        name_list.add(persons.get(i).getName());
//                    }
//                    myAdapter.setNames(name_list);
//
//                    recyclerView.setAdapter(myAdapter); //必須放在 myAdapter.setNames 之後做
//                    myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener(){
//                        @Override
//                        public void onItemClick(View view , int position){
//                            Log.i("suvini", "position : " + position);
//                        }
//                    });
//                    id.setText(persons.get(0).getId()); //設定0，因為只想抓第一筆即可
//                    nodeId.setText(persons.get(0).getNodeId());
////                    name.setText(nameString);
//                    fullName.setText(persons.get(0).getfull_name());
//                    Toast.makeText(getContext(), "HTML代码加载完毕", Toast.LENGTH_SHORT).show();
//                    Log.i("suvini", "persons.get(0).getId() : " + persons.get(0).getId());
                    break;
                default:
                    break;
            }
        }
    };
}
