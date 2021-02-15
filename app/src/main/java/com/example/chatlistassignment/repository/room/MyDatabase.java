package com.example.chatlistassignment.repository.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.model.User;

@Database(entities = {User.class, Contact.class}, version = 2, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase INSTANCE;

    public static void initInstance(Context context){
        INSTANCE = Room.databaseBuilder(context, MyDatabase.class, "User_Database")
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    public static MyDatabase getInstance(Context context) {
        if (INSTANCE == null) MyDatabase.initInstance(context);
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS 'ContactDB' (" +
                    "'id' TEXT NOT NULL," +
                            "'name' TEXT," +
                            "'number' TEXT," +
                    " PRIMARY KEY('id'));");
        }
    };

    public abstract UserDao userDao();
    public abstract ContactDao contactDao();
}
