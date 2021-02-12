package com.example.chatlistassignment.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactsChangeListener {
    private ContentResolver mContentResolver;
    private ContactsChangeObserver mContactsChangeObserver;

    private static ContactsChangeListener mContactsChangeListener;
    private static IChangeListener mListener;

    private ContactsChangeListener(Context mContext) {
        mContentResolver = mContext.getContentResolver();
    }

    public static ContactsChangeListener getInstance(Context context) {
        if (mContactsChangeListener == null) {
            mContactsChangeListener = new ContactsChangeListener(context);
        }
        return mContactsChangeListener;
    }


    public void startContactsObservation(IChangeListener iChangeListener) {
        mContactsChangeObserver = new ContactsChangeObserver(new Handler(Looper.getMainLooper()));
        mContentResolver.registerContentObserver(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, true, mContactsChangeObserver);
        mListener = iChangeListener;

    }

    public void stopContactsObservation() {
        mContentResolver.unregisterContentObserver(mContactsChangeObserver);
        mListener = null;
    }


    private class ContactsChangeObserver extends ContentObserver {

        public ContactsChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mListener.onContactsChanged();
        }

//        @Override
//        public void onChange(boolean selfChange) {
//            super.onChange(selfChange);
//            //long mLastTimeChangeOccured = System.currentTimeMillis();
//            //if (mLastTimeChangeOccured - mLastChangeProcessed > MIN_WAIT_TIME) {
//                notifyContactChanged();
////                Log.e("abc", "onChange:Contacts Changed , now call sync  ----------->> ");
////                mLastChangeProcessed = System.currentTimeMillis();
//            //}
//        }
    }
}
