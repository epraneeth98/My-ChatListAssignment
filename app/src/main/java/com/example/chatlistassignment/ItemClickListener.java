package com.example.chatlistassignment;

import android.view.View;

import com.example.chatlistassignment.model.User;

public interface ItemClickListener {
    void onItemClicked(View view, int position);

    void onItemLongClicked(View v, int position, int adapterPosition);
}
