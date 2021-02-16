package com.example.chatlistassignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chatlistassignment.R;
import com.example.chatlistassignment.adapters.ListViewAdapter;
import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.utils.HelperFunctions;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactDetailsActivity extends AppCompatActivity {

    ListView listView;
    ListViewAdapter listViewAdapter;
    TextView contact_details_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        init();
    }

    private void init() {
        ArrayList<String> ars = getIntent().getStringArrayListExtra("Contact_all_numbers");
        listView = findViewById(R.id.contact_details_list_view);
        contact_details_name = findViewById(R.id.contact_details_name);
        ArrayList<Pair<String, String>> mTypeNumberList = new ArrayList<>();
        for(String s: ars){
            mTypeNumberList.add(HelperFunctions.splitString(s));
        }
        contact_details_name.setText(getIntent().getStringExtra("contact_name"));
        listViewAdapter = new ListViewAdapter(this, mTypeNumberList);
        listView.setAdapter(listViewAdapter);
    }
}