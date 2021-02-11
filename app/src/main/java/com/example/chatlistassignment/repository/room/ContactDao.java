package com.example.chatlistassignment.repository.room;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.chatlistassignment.model.Contact;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addContactListToDb(List<Contact> contactList);

    @Query("select * from ContactDB order by name desc")
    DataSource.Factory<Integer, Contact> getAllContacts();

    @Query("select * from contactdb where name like :query or number like :query order by name asc")
    DataSource.Factory<Integer, Contact> getQueryContacts(String query);
}
