package com.example.chatlistassignment.repository.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.chatlistassignment.model.MyTypeConverter;
import com.example.chatlistassignment.model.User;

@Database(entities = User.class, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    private static UserDatabase INSTANCE;

    public static UserDatabase getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context, UserDatabase.class, "User_Database").build();
//            Room.databaseBuilder(context, UserDatabase.class, "User_Database")
//                .addMigrations(MIGRATION_1_2).build();
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE User "
                    + " ADD COLUMN joining_year INTEGER");
        }
    };

    public abstract UserDao userDao();
}
