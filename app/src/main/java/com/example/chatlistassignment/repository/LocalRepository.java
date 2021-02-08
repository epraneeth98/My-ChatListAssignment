package com.example.chatlistassignment.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.repository.room.UserDao;
import com.example.chatlistassignment.repository.room.UserDatabase;
import java.util.List;
import io.reactivex.Completable;

public class LocalRepository {

    private UserDao userDao;

    public LocalRepository(Context ctx) {
        userDao = UserDatabase.getInstance(ctx).userDao();
    }

    public Completable addUser(User user) {
        return userDao.addUser(user);
    }

    public Completable deleteUser(User user) {
        return userDao.deleteUser(user);
    }

    public Completable updateUser(User user) {
        return userDao.updateUser(user);
    }

    public LiveData<List<User>> getAllUser() {
        return userDao.getAllUser();
    }

    public LiveData<List<User>> queryAllUser(String query) {
        return userDao.queryAllUser(query);
    }

}
