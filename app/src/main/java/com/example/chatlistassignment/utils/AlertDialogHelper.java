package com.example.chatlistassignment.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.example.chatlistassignment.fragments.ChatListFragment;

import static java.security.AccessController.getContext;

public class AlertDialogHelper {
    Context context;
    AlertDialog alertDialog = null;
    AlertDialogListener callBack;
    Activity current_activity;
    ChatListFragment chatListFragment;

    public AlertDialogHelper(Context context, ChatListFragment chatListFragment) {
        this.context = context;
        this.current_activity = (Activity) context;
        this.chatListFragment = chatListFragment;
        callBack = (AlertDialogListener) chatListFragment;
    }

    public void showAlertDialog(String title, String message, String positive, String negative, final int from, boolean isCancelable) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(positive)) {
            alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#FF0000'>" + positive + "</font>"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            callBack.onPositiveClick(from);
                            alertDialog.dismiss();
                        }
                    });
        }
        if (!TextUtils.isEmpty(negative)) {
            alertDialogBuilder.setNegativeButton(negative,
                    (arg0, arg1) -> {
                        callBack.onNegativeClick(from);
                        alertDialog.dismiss();
                    });
        }
        alertDialogBuilder.setCancelable(isCancelable);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public interface AlertDialogListener {
        void onPositiveClick(int from);

        void onNegativeClick(int from);

    }
}

