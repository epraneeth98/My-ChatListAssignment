package com.example.chatlistassignment.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.repository.LocalRepository;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;
import com.google.gson.annotations.Since;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class SyncNativeContacts {
    Context context;

    public SyncNativeContacts(Context context) {
        this.context = context;
    }

    public Single<List<Contact>> getContactArrayList() {
        return Single.fromCallable(() -> {
            int count = 0;
            List<Contact> contactList = new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    List<String> numberList = new ArrayList<>();
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                new String[]{id}, null);
                        while (phoneCursor.moveToNext()) {
                            String number = phoneCursor.getString(
                                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            numberList.add(number);
                            Log.d("TAG", "Name is: " + name);
                            Log.d("TAG", "Number is: " + number);
                            count++;
                        }
                        phoneCursor.close();
                    }
                    Contact contact = new Contact(id, name, numberList);
                    contactList.add(contact);
                }
                cursor.close();
            }
            Log.d("abc", "In SyncNativeContacts, contactsCount is: " + count);
            FragmentViewModel.setContactsCount(count);
            return contactList;

        });
    }

}
