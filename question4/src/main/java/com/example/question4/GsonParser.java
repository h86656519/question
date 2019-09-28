package com.example.question4;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class GsonParser {
    public ArrayList<Repo> parse(String jsonString){
//       Log.i("suvini", "GsonParser : " + jsonString);
        Gson gson = new Gson();
//        ArrayList<Repo> person = gson.fromJson(jsonString, new TypeToken<ArrayList<Repo>>(){}.getType());
        Repo temp = gson.fromJson(jsonString, new TypeToken<Repo>(){}.getType()); //要抓得資料就只有一個array
        ArrayList<Repo> person = new ArrayList<Repo>();
        person.add(temp); //將資料中的 "每一組資料(Repo)" 放到ArrayList 裡
        return person;
    }
}
