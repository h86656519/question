package com.example.question4;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class GsonParser {
    public ArrayList<Repo> parse(String jsonString){
      //  Log.i("suvini", "GsonParser : " + jsonString);
        Gson gson = new Gson();
//        ArrayList<Repo> person = gson.fromJson(jsonString, new TypeToken<ArrayList<Repo>>(){}.getType());
        Repo temp = gson.fromJson(jsonString, new TypeToken<Repo>(){}.getType());
        ArrayList<Repo> person = new ArrayList<Repo>();
        person.add(temp);
        Log.i("suvini", "person : " + person.size());
        return person;
    }
}
