package com.example.question4;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {
    public static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        inStream.close();
      //  Log.e("TAG", "outStream.toByteArray() : " + outStream.toByteArray());
        return outStream.toByteArray();
    }
}
