package com.example.chatlistassignment.repository;

import android.content.Context;

import androidx.paging.DataSource;

import com.example.chatlistassignment.MyApplicationClass;
import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.repository.room.ContactDao;
import com.example.chatlistassignment.repository.room.MyDatabase;
import com.example.chatlistassignment.repository.room.UserDao;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.HiltAndroidApp;
import io.reactivex.Completable;
import io.reactivex.Single;

public class LocalRepository {

    private UserDao userDao;
    private ContactDao contactDao;

    @Inject
    public LocalRepository(Context ctx, UserDao userDao, ContactDao contactDao) {
        this.userDao = userDao;
        //userDao = MyDatabase.getInstance(ctx).userDao();
        this.contactDao = contactDao;
        //contactDao = MyDatabase.getInstance(ctx).contactDao();
    }

    /////////User
    public Completable addUser(User user) {
        return userDao.addUser(user);
    }

    public Completable deleteUser(User user) {
        return userDao.deleteUser(user);
    }

    public Completable updateUser(User user) {
        return userDao.updateUser(user);
    }

    public DataSource.Factory<Integer, User> getAllUser() {
        return userDao.getAllUser();
    }

    public DataSource.Factory<Integer, User> queryAllUser(String query) {
        return userDao.queryAllUser(query);
    }

    ///////Contact
    public Completable addContactListToDb(List<Contact> contactList){
        return contactDao.addContactListToDb(contactList);
    }

    public DataSource.Factory<Integer, Contact> getAllContacts(){
        return contactDao.getAllContacts();
    }

    public DataSource.Factory<Integer, Contact> getQueryContact(String query){
        return contactDao.getQueryContacts(query);
    }

    //public Single getContactsCount()
}
