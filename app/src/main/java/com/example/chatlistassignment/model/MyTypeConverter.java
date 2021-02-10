package com.example.chatlistassignment.model;

import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


public class MyTypeConverter {

    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
