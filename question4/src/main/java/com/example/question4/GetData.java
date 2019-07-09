package com.example.question4;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetData implements Runnable {
    private String HTML_URL = "";
    private GsonParser gsonParser = new GsonParser();
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    Context mcontext;
    RunnableListener listener;
    public GetData(String HTML_URL, Context context) {
        this.HTML_URL = HTML_URL;
        this.mcontext = context;
    }

    public void setListener(RunnableListener listener) {
        this.listener = listener;
    }

    public interface RunnableListener
    {
        void onResult(ArrayList<Repo> person);
    }
    private RunnableListener runnableListener;

//    public static String getHtmlwithtoken(String path) throws Exception {
//        URL url = new URL(path);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET"); //GET请求不支持请求体，只有POST请求才能设置请求体
//
//        Log.i("suviniii", "getResponseCode : " + conn.getResponseCode());
//        if (conn.getResponseCode() == 200) {
//            InputStream in = conn.getInputStream();
//            byte[] data = StreamTool.read(in);
//            String html = new String(data, "UTF-8");
//            Log.i("suviniii", "html : " +html);
//            return html;
//        }
//        return null;
//    }


    private void someMethod() {
        new Handler().post(new Runnable() {
            @Override public void run() {

                try {
                    URL url = new URL(HTML_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET"); //GET请求不支持请求体，只有POST请求才能设置请求体
                    Log.i("suviniii", "getResponseCode : " + conn.getResponseCode());
                    if (conn.getResponseCode() == 200) {
                        Message message = new Message();
                        InputStream in = conn.getInputStream();
                        byte[] data = StreamTool.read(in);
                        String html = new String(data, "UTF-8");
                        Log.i("suviniii", "html : " + html);
                        ArrayList<Repo> persons = gsonParser.parse(html.toString());



                        // return html;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void run() {
        try {
            URL url = new URL(HTML_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET"); //GET请求不支持请求体，只有POST请求才能设置请求体
            Log.i("suviniii", "getResponseCode : " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                Message message = new Message();
                InputStream in = conn.getInputStream();
                byte[] data = StreamTool.read(in);
                String html = new String(data, "UTF-8");
                Log.i("suviniii", "html : " + html);
                ArrayList<Repo> persons = gsonParser.parse(html.toString());
                runnableListener.onResult(persons);
                message.obj = persons;
                Handler handler = new Handler();
                handler.sendMessage(message);
                // return html;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private Handler handler = new Handler() {
////        public void handleMessage(android.os.Message msg) {
////            switch (msg.what) {
////                case 0x002:
////                    ArrayList<Repo> list = (ArrayList<Repo>) msg.obj;
////                    myAdapter.setDatelist(list);
////                    recyclerView.setAdapter(myAdapter); //必須放在 myAdapter.setNames 之後做
////                    // Toast.makeText(MainActivity.this, "讀取完畢", Toast.LENGTH_SHORT).show();
//////                    Log.i("suvini", "persons.get(0).getId() : " + persons.get(0).getName());
////                    break;
////                default:
////                    break;
////            }
////        }
////    };

}
