package com.example.chatlistassignment.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HelperFunctions {
    public static long getDateinMilli(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0,0,0);
        return calendar.getTimeInMillis();
    }

    public static int getDay(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.get(Calendar.YEAR);
    }

    public static String getHeaderText(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        //String month = months[calendar.get(Calendar.MONTH)-1].substring(0,2);
        ///to do some stuff
        Log.d("abc", "milliseconds gotten: "+milliSeconds);
        return calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.YEAR);
    }

}
