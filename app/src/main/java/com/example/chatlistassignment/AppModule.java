package com.example.chatlistassignment;

import android.content.Context;

import androidx.room.Database;

import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.repository.LocalRepository;
import com.example.chatlistassignment.repository.room.ContactDao;
import com.example.chatlistassignment.repository.room.MyDatabase;
import com.example.chatlistassignment.repository.room.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ApplicationComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public static MyDatabase provideMyDatabase(@ApplicationContext Context context) {
        return MyDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static UserDao provideUserDao(@ApplicationContext Context context){
        return MyDatabase.getInstance(context).userDao();
    }

    @Provides
    @Singleton
    public static ContactDao provideContactDao(@ApplicationContext Context context){
        return MyDatabase.getInstance(context).contactDao();
    }

    @Provides
    @Singleton
    public static LocalRepository provideLocalRepository(@ApplicationContext Context context, UserDao userDao, ContactDao contactDao){
        return new LocalRepository(context, userDao, contactDao);
    }


}
