package com.example.chatlistassignment;

import android.app.Application;

import com.example.chatlistassignment.repository.room.MyDatabase;

public class MyApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyDatabase.initInstance(getApplicationContext());
    }
}
