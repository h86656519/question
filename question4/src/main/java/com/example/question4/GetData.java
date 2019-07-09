package com.example.question4;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetData implements Runnable {
    private String HTML_URL = "";
    private GsonParser gsonParser = new GsonParser();
    RunnableListener listener;

    public GetData(String HTML_URL) {
        this.HTML_URL = HTML_URL;
    }
    public void setListener(RunnableListener listener) {
        this.listener = listener;
    }

    public interface RunnableListener {
        void onResult(ArrayList<Repo> person);
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
                ArrayList<Repo> persons = gsonParser.parse(html.toString());
//                Log.i("suviniii", "html : " + html);
                listener.onResult(persons); //用listiner 回傳抓下來的資料
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
