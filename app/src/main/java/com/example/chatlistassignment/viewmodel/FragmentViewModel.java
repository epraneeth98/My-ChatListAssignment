package com.example.chatlistassignment.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatlistassignment.R;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.repository.LocalRepository;
import com.example.chatlistassignment.repository.room.UserDatabase;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentViewModel extends AndroidViewModel {

    private LocalRepository repository;
    private Toast toast;

    public static MutableLiveData<String> queryString = new MutableLiveData<>();

    public FragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new LocalRepository(getApplication());
    }

    public static void setQueryString(String query) {
        queryString.setValue(query);
    }

    public LiveData<String> getQueryString() {
        return queryString;
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

    public LiveData<List<User>> getAllUser(Context context) {
        return repository.getAllUser();
    }

    public LiveData<List<User>> queryAllUser(Context context, String query) {
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

        view.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);

        toast.show();
    }

    private void NumberExistsToast(String message, Context context) {

        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();

        toast.show();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //compositeDisposable.dispose();
    }
}