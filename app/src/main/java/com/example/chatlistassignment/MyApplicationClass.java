package com.example.chatlistassignment;

import android.app.Application;

import com.example.chatlistassignment.repository.room.MyDatabase;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyDatabase.initInstance(getApplicationContext());
    }
}
