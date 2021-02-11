package com.example.chatlistassignment.utils;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HelperFunctions {
    public static long getDateinMilli(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
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
        String month = months[calendar.get(Calendar.MONTH)].substring(0, 3).toUpperCase();
        Log.d("abc", "milliseconds gotten: " + milliSeconds);
        if (DateUtils.isToday(milliSeconds)) return "TODAY";
        else if (DateUtils.isToday(milliSeconds + 86400000)) return "YESTERDAY";
        return calendar.get(Calendar.DAY_OF_MONTH) + " " + month;// + "-" + calendar.get(Calendar.YEAR);
    }

    public static String checkNumber(String number) {
        int len = number.length();
        if (len > 12) return "Invalid";
        else if (number.charAt(0) == '+') {
            if (len <= 10) return "Invalid";
            else return number;
        } else {
            if (len >= 10) {
                return "+91" + number;
            } else return "Invalid";
        }
    }

}
