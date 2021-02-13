package com.example.chatlistassignment.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;

import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.repository.LocalRepository;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;
import com.google.gson.annotations.Since;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

public class SyncNativeContacts {
    Context context;
    String labels[] = {"JIO",
            "HOME",
            "MOBILE",
            "WORK",
            "FAX_WORK",
            "FAX_HOME",
            "PAGER",
            "OTHER",
            "CALLBACK",
            "CAR",
            "COMPANY_MAIN",
            "ISDN",
            "MAIN",
            "OTHER_FAX",
            "TYPE_RADIO",
            "TYPE_TELEX",
            "TYPE_TTY_TDD",
            "TYPE_WORK_MOBILE",
            "TYPE_WORK_PAGER",
            "TYPE_ASSISTANT",
            "TYPE_MMS"};

    public SyncNativeContacts(Context context) {
        this.context = context;
    }

    public Single<List<Contact>> getContactArrayList() {
        return Single.fromCallable(this::call);
    }

    private List<Contact> call() {
        int count = 0;
        List<Contact> contactList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                count++;
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                List<Pair<String, String>> numberList = new ArrayList<>();
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,// null,null,null);
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{id}, null);
                    int numbers_in_contact_count = 0;
                    Map<String, String> map = new HashMap<>();

                    while (phoneCursor != null && phoneCursor.moveToNext()) {
                        numbers_in_contact_count++;
                        String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String type = labels[Integer.parseInt(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)))];
                        //String type = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
                        number = makeCorrections(number);
                        //numberList.add(Pair.create(type, number));
//                        Log.d("TAG", "Name is: " + name);
//                        Log.d("TAG", "Number is: " + type + "  " + number);
                        if (map.get("number") == null) map.put(number, type);
                    }
                    Log.d("TAG", "Name is: " + name);
                    Log.d("TAG", "Numbers in contactc count is: " + map.size());

                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        numberList.add(Pair.create(entry.getKey(), entry.getValue()));
                        Log.d("TAG", "Entry key: " + entry.getKey());
                    }

                    phoneCursor.close();

                }
//                List<Pair<String,String>> list;
//                for(Pair<String, String> s: numberList){
//
//                }
                Contact contact = new Contact(id, name, numberList);
                contactList.add(contact);
            }
            cursor.close();
        }
        Log.d("abc", "In SyncNativeContacts, contactsCount is: " + count);
        FragmentViewModel.setContactsCount(count);
        return contactList;

    }

    private String makeCorrections(String test) {
        test = test.replaceAll("\\p{P}", "");
        test = test.replaceAll(" ", "");

        if (test.charAt(0) != '+' && test.length()==10) test = "+91" + test;
        return test;
    }
}
