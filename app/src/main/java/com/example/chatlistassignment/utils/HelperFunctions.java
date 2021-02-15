package com.example.chatlistassignment.utils;

import android.text.format.DateUtils;
import android.util.Log;
import android.util.Pair;

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
        return calendar.get(Calendar.MONTH)+1;
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
        String month = months[calendar.get(Calendar.MONTH)].substring(0, 3);
        Log.d("abc", "milliseconds gotten: " + milliSeconds);
        if (DateUtils.isToday(milliSeconds)) return "Today";
        else if (DateUtils.isToday(milliSeconds + 86400000)) return "Yesterday";
        return calendar.get(Calendar.DAY_OF_MONTH) + " " + month;// + "-" + calendar.get(Calendar.YEAR);
    }

    public static String checkNumber(String number) {
        int len = number.length();
        if (len > 13) return "Invalid";
        else if (number.charAt(0) == '+') {
            if (len <= 10) return "Invalid";
            else return number;
        } else {
            if (len >= 10) {
                return "+91" + number;
            } else return "Invalid";
        }
    }

    public static Pair<String, String> splitString(String s) {
        String[] words = s.split(":");
        if (words.length == 1) {
            Log.d("abc", "Anomaly: " + s);
            return Pair.create(words[0], words[0]);
        }
        if (words.length == 0) return Pair.create("Not Available", "Not Available");
        return Pair.create(words[0], words[1]);
    }

}
