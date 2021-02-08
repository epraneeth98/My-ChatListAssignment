package com.example.chatlistassignment.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.chatlistassignment.R;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.utils.SaveBitmap;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Calendar;

public class EditUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout editTextName, editTextNumber;
    Button buttonSave;
    TextView textViewBirthday, buttonDatePicker;
    ImageView imageView, buttonImageSelect;
    String ProfilePicPath = null;
    int uid;

    FragmentViewModel fragmentViewModel;
    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        User user = (User) getIntent().getSerializableExtra("User");
        uid = user.getUid();
        ProfilePicPath = user.getProfilePic();
        fragmentViewModel = ViewModelProviders.of(this).get(FragmentViewModel.class);
        init(user);
    }

    private void init(User user) {
        editTextName = findViewById(R.id.edit_text_name);
        editTextNumber = findViewById(R.id.text_view_number);
        buttonDatePicker = findViewById(R.id.text_view_birthday);
        buttonSave = findViewById(R.id.button_save);
        buttonImageSelect = findViewById(R.id.image_view_profile_pic);
        textViewBirthday = findViewById(R.id.text_view_birthday);
        imageView = findViewById(R.id.image_view_profile_pic);
        if (user.getProfilePic() == null) {
            imageView.setImageResource(R.drawable.ic_baseline_person_24);
            Glide.with(this)
                    .load(R.drawable.ic_baseline_person_24)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .circleCrop()
                    .into(imageView);
        } else {
            imageView.setImageURI(Uri.parse(user.getProfilePic()));
            Glide.with(this)
                    .load(Uri.parse(user.getProfilePic()))
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .circleCrop()
                    .into(imageView);

        }

        buttonDatePicker.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonImageSelect.setOnClickListener(this);

        editTextName.getEditText().setText(user.getName());
        editTextNumber.getEditText().setText(user.getContactNumber());

        String DOBBuilder = "Date of Birth: " + user.getDateOfBirth();
        textViewBirthday.setText(DOBBuilder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_birthday:
                buttonDatePickerClicked();
                break;
            case R.id.button_save:
                saveButtonClicked();
                break;
            case R.id.image_view_profile_pic:
                buttonSelectProfilePicClicked();
                break;
        }
    }

    private void buttonSelectProfilePicClicked() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals("Take Photo")) {
                    checkPermissionAndStartCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    checkPermissionAndOpenGallery();
                } else
                    dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        else
            startOpenGalleryIntent();
    }

    private void startOpenGalleryIntent() {
        Intent intentPickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentPickPhoto, REQUEST_CODE_GALLERY);
    }

    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        else
            startTakePictureIntent();
    }

    private void startTakePictureIntent() {
        Intent intentTakePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePicture, REQUEST_CODE_CAMERA);
    }

    private void buttonDatePickerClicked() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            month = month + 1; // As Jan starts from 0
            String date = "Date of Birth:" + dayOfMonth + "/" + month + "/" + year;
            textViewBirthday.setText(date);
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    Bitmap bitmapCameraImage = (Bitmap) data.getExtras().get("data");
                    Uri cameraImageUri = null;
                    try {
                        Log.d("TAG", "Inside try of onActivity result of DataEntryFragment");
                        cameraImageUri = SaveBitmap.saveBitmapReturnUri(bitmapCameraImage);
                        Log.d("TAG", "cmeraUri: " + cameraImageUri.toString());
                        Log.d("TAG", "cameraUri: " + cameraImageUri.getPath());

                    } catch (IOException e) {
                        Log.d("TAG", "Inside catch: " + e.getMessage());
                        e.printStackTrace();
                    }
                    imageView.setImageURI(cameraImageUri);
                    Glide.with(this)
                            .load(cameraImageUri)
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .circleCrop()
                            .into(imageView);

                    ProfilePicPath = cameraImageUri.toString();
                    break;

                case REQUEST_CODE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    Log.d("TAG", "URi: " + selectedImageUri.getPath());
                    ProfilePicPath = selectedImageUri.toString();

                    imageView.setImageURI(selectedImageUri);
                    Glide.with(this)
                            .load(selectedImageUri)
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .circleCrop()
                            .into(imageView);
                    break;
            }
        }
    }

    private void saveButtonClicked() {
        String userName = editTextName.getEditText().getText().toString();
        String contactNumber = editTextNumber.getEditText().getText().toString();

        if (userName.equals("") || contactNumber.equals("")) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        String birthDateString = textViewBirthday.getText().toString();
        int indexOfColon = birthDateString.indexOf(":");
        String birthDate = birthDateString.substring(indexOfColon + 1);

        Log.d("abc", "Here pfp: " + ProfilePicPath);
        User userUpdated = new User(uid, userName, contactNumber, ProfilePicPath, birthDate);
        fragmentViewModel.updateUser(userUpdated, this);
        ProfilePicPath = null;

        onBackPressed();
    }
}