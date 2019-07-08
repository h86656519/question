package com.example.question4;

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

    public GetData(String HTML_URL) {
        this.HTML_URL = HTML_URL;
    }

    public static synchronized String getHtmlwithtoken(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET"); //GET请求不支持请求体，只有POST请求才能设置请求体

        Log.i("suviniii", "getResponseCode : " + conn.getResponseCode());
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = StreamTool.read(in);
            String html = new String(data, "UTF-8");
            Log.i("suviniii", "html : " +html);
            return html;
        }
        return null;
    }

    @Override
    public void run() {
        try {
            getHtmlwithtoken(HTML_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
