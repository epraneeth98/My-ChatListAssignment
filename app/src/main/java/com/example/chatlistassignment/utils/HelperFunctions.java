package com.example.chatlistassignment.utils;

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
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long today = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -1);
        long yesterday = calendar.getTimeInMillis();
        calendar.setTimeInMillis(milliSeconds);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String month = months[calendar.get(Calendar.MONTH)-1].substring(0,3);
        ///to do some stuff
        return getDay(milliSeconds) + " " + month + " " + getYear(milliSeconds);
    }

}
