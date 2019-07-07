package com.example.question4;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Why still need Handler, why not just use HttpListener directly?
//What's the difference between use UrlConnection in Activity and use HttpHelper in Activity?
public class HttpHelper {
    private static HttpHelper httpHelper = new HttpHelper();

    public static HttpHelper getInstance() {
        return httpHelper;
    }

    //------------------------------------------------------

    private Response response;

    public HttpListener getListener() {
        return listener;
    }

    public void setListener(HttpListener listener) {
        this.listener = listener;
    }

    private HttpListener listener;
    private int timeout = 5000;
    private String path;
    private String method;
    private String token = null;

    public void setMethod(String method) {
        this.method = method;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Response getResponse() {
        return response;
    }

    // ExecutorService 負責管理 Thread
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void request() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final Response response = new Response();
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(timeout);
                    conn.setRequestMethod(method);
                    if (method != null) {
                        conn.setRequestProperty("Authorization", "token" + " " + token);
                    }

                    Message message = new Message();
                    message.what = 0x03;

                    Log.i("suvini", "getResponseCode : " + conn.getResponseCode());
                    response.setHttpCode(conn.getResponseCode());
                    if (conn.getResponseCode() == 200) {
                        InputStream in = conn.getInputStream();
                        byte[] data = StreamTool.read(in);
                         String html = new String(data, "UTF-8");
                        response.setJson(new String(data, "UTF-8"));
                          Log.i("suviniii", "html : " + html);

                        message.obj = response;
                        handler.sendMessage(message);
                    } else if (conn.getResponseCode() == 401) {
                        Log.i("suvini", "401 訪問權限不夠，確認一下token");
                    }
                    response.setErrorMessage(conn.getResponseMessage());

                    message.obj = response;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class Response {
        private String json;
        private int httpCode; //404, 200
        private String errorMessage; //Wrong url

        public String getJson() {
            return json;
        }

        public int getHttpCode() {
            return httpCode;
        }

        public void setJson(String json) {
            this.json = json;
        }

        public void setHttpCode(int httpCode) {
            this.httpCode = httpCode;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

    }


    public interface HttpListener {
        public void onSuccess(Response response);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x003:
                    response = (Response) msg.obj;
                    if (listener != null) {
                        listener.onSuccess(response);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void Destroy() {
        executorService.shutdown();

    }
}
