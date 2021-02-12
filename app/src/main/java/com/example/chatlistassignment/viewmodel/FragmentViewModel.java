package com.example.chatlistassignment.viewmodel;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.repository.LocalRepository;
import com.example.chatlistassignment.repository.room.MyDatabase;
import com.example.chatlistassignment.utils.SyncNativeContacts;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentViewModel extends AndroidViewModel {

    public final static String TAG = "TAG";
    private LocalRepository repository;
    private Toast toast;
    public LiveData<PagedList<User>> userList;
    public LiveData<PagedList<User>> queriedUserList;
    public LiveData<PagedList<Contact>> contactList;
    public LiveData<PagedList<Contact>> queryContactList;

    public static MutableLiveData<Integer> contactsCount = new MutableLiveData<>();
    public static MutableLiveData<String> queryString = new MutableLiveData<>();

    public static void setQueryString(String query) {
        queryString.setValue(query);
    }

    public LiveData<String> getQueryString() {
        return queryString;
    }

    public static void setContactsCount(Integer contactsCountValue) {
        contactsCount.postValue(contactsCountValue);
    }

    public LiveData<Integer> getContactsCount() {
        return contactsCount;
    }

    public FragmentViewModel(@androidx.annotation.NonNull Application application) {
        super(application);
        repository = new LocalRepository(getApplication());
        userList = new LivePagedListBuilder<>(
                repository.getAllUser(), /* page size */ 8).build();
        contactList = new LivePagedListBuilder<>(
                repository.getAllContacts(), 15).build();
    }

    public void queryInit(String query) {
        repository = new LocalRepository(getApplication());
        queriedUserList = new LivePagedListBuilder<>(repository.queryAllUser(query), 8).build();
    }

    public void queryContactInit(String query) {
        repository = new LocalRepository(getApplication());
        queryContactList = new LivePagedListBuilder<>(repository.getQueryContact(query), 15).build();
    }


    public void addUser(User user, Context context) {
        repository.addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of addUser in ViewModel");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of addUser in ViewModel");
                        successToast("User added successfully", context);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of addUser in ViewModel." + e.getMessage());
                        NumberExistsToast("Number Already Exists", context);
                    }
                });
    }

//    public DataSource.Factory<Integer, User> getAllUser(Context context) {
//        return repository.getAllUser();
//    }

    public DataSource.Factory<Integer, User> queryAllUser(Context context, String query) {
        return repository.queryAllUser(query);
    }

    public void deleteUser(User user, Context context) {
        repository.deleteUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of deleteUser in ViewModel");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of deleteUser in ViewModel");
                        successToast("User data removed successfully", context);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of deleteUser in ViewModel");
                        failureToast(e.getMessage(), context);
                    }
                });
    }

    public void updateUser(User user, Context context) {
        repository.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of updateUser in ViewModel");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of updateUser in ViewModel");
                        successToast("User Data updated successfully", context);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of updateUser in ViewModel");
                        NumberExistsToast("Number Already Exists, Data cannot be saved", context);
                    }
                });
    }


    private void successToast(String message, Context context) {

        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        //view.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.teal_200), PorterDuff.Mode.SRC_IN);

        toast.show();
    }

    private void failureToast(String message, Context context) {

        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();

        //view.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);

        toast.show();
    }

    private void NumberExistsToast(String message, Context context) {

        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();

        toast.show();
    }

    public void completeContactSync() {
        SyncNativeContacts syncNativeContacts = new SyncNativeContacts(getApplication());
        syncNativeContacts.getContactArrayList().doAfterSuccess(contacts ->
                addContactsToDb(contacts))
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Contact>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: Inside complete sync  ");
                    }

                    @Override
                    public void onSuccess(@NonNull List<Contact> contacts) {
                        Log.e(TAG, "onSuccess: Inside complete sync: " + contacts.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: Inside complete sync error: " + e.getMessage());
                    }
                });

    }

    private void addContactsToDb(List<Contact> contactList) {
        repository.addContactListToDb(contactList)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of addContactListDB");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of addContactListDB");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of addContactListDB");
                    }
                });
    }

}