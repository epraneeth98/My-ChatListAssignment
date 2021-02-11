package com.example.chatlistassignment.repository.room;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.chatlistassignment.model.User;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface UserDao {

    @Insert
    Completable addUser(User user);

    @Delete
    Completable deleteUser(User user);

    @Query("select * from userdb Order by dateOfBirth desc")
    DataSource.Factory<Integer, User> getAllUser();

    @Update
    Completable updateUser(User user);

    @Query("select * from userdb where name like :query")
    DataSource.Factory<Integer, User> queryAllUser(String query);
}
