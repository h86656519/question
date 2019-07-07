package com.example.question4;

import android.util.Log;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData {
    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000); //設定超時的時間
        conn.setRequestMethod("GET"); //設定請求的方式
        if (conn.getResponseCode() != 200) { //200才是成功
            throw new RuntimeException("連接失敗");
        }
        InputStream inStream = conn.getInputStream();
        byte[] bt = StreamTool.read(inStream);
        inStream.close();

        return bt;
    }

    // 获取网页的html源代码
    public static String getHtml(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = StreamTool.read(in);
            String html = new String(data, "UTF-8");
            return html;
        }
        return null;
    }

    public static String getHtmlwithtoken(String path) throws Exception {
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

    public static String putHtml(String path, String body,String token) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000); //设置连接超时时间
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("X-Auth-Token", "token");  //设置请求的token
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "token" + " " + token);

        PrintWriter dos = new PrintWriter(conn.getOutputStream());
        dos.print(body);
        dos.flush();
        dos.close();

        //写入请求参数
        //这里要注意的是，在构造JSON字符串的时候，实践证明，最好不要使用单引号，而是用“\”进行转义，否则会报错
        // 关于这一点在上面给出的参考文章里面有说明
        Log.i("suviniii", "conn.getResponseCode() : " + conn.getResponseCode());
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = StreamTool.read(in);
            String html = new String(data, "UTF-8");
            Log.i("suviniii", "" + html);
            return html;
        }
        conn.disconnect();
        return null;
    }
    public static String deletHtml(String path, String body,String token) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000); //设置连接超时时间
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("X-Auth-Token", "token");  //设置请求的token
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "token" + " " + token);

        PrintWriter dos = new PrintWriter(conn.getOutputStream());
        dos.print(body);
        dos.flush();
        dos.close();

        //写入请求参数
        //这里要注意的是，在构造JSON字符串的时候，实践证明，最好不要使用单引号，而是用“\”进行转义，否则会报错
        Log.i("suviniii", "conn.getResponseCode() : " + conn.getResponseCode());
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = StreamTool.read(in);
            String html = new String(data, "UTF-8");
            Log.i("suviniii", "刪除成功");
            return html;
        }else if(conn.getResponseCode() == 404){
            Log.i("suviniii", "找不到擋案，是否已經刪除了?");
        }
        conn.disconnect();
        return null;
    }
}
