package com.example.chatlistassignment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.chatlistassignment.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        init();
    }

    private void init() {
        Log.d("abc", "In FullScreenImageactivity");
        imageView = findViewById(R.id.imageView);
        Log.d("abc", "profile pic path in Activity: "+getIntent().getStringExtra("imageUri"));
        Glide.with(this)
                .load(Uri.parse(getIntent().getStringExtra("imageUri")))
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(imageView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}