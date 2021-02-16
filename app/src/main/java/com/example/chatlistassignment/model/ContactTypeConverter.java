package com.example.chatlistassignment.model;

import android.text.TextUtils;
import android.util.Pair;

import androidx.room.TypeConverter;

import com.example.chatlistassignment.utils.HelperFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactTypeConverter {
    @TypeConverter
    public static List<Pair<String, String>> stringToList(String stringList) {
        List<String> tempList = Arrays.asList(stringList.split(","));
        List<Pair<String, String>> ans = new ArrayList<>();
        for(String s: tempList){
            //List<String> temp = Arrays.asList(s.split(":"));
            //ans.add(Pair.create(temp.get(0), temp.get(0)));
            ans.add(HelperFunctions.splitString(s));
        }
        return ans;
    }

    @TypeConverter
    public static String listToString(List<Pair<String, String>> listString) {
        List<String> tempList = new ArrayList<>();
        for(Pair<String, String> p: listString){
            tempList.add(p.first+":"+p.second);
        }
        return TextUtils.join(",", tempList);
    }
}

