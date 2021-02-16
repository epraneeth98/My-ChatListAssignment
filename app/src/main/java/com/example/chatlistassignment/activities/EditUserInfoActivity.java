package com.example.chatlistassignment.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.chatlistassignment.R;
import com.example.chatlistassignment.fragments.DataEntryFragment;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.utils.HelperFunctions;
import com.example.chatlistassignment.utils.SaveBitmap;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditUserInfoActivity extends AppCompatActivity{

    TextInputLayout editTextName, editTextNumber, editTextNumber2, editTextNumber3;
    Button buttonSave;
    ImageButton addAnotherNumberButton, addAnotherNumberButton2;
    TextView textViewBirthday;
    LinearLayout linearLayout2;
    ImageView imageView, buttonImageSelect;
    String ProfilePicPath = null;
    int uid;
    long dateInMilliSeconds = 0;

    FragmentViewModel fragmentViewModel;
    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        User user = (User) getIntent().getSerializableExtra("User");
        uid = user.getUid();
        //ProfilePicPath = user.getProfilePic();
        fragmentViewModel = ViewModelProviders.of(this).get(FragmentViewModel.class);
        //init(user);
        init2(user);
    }

    private void init2(User user) {
        Log.d("abc", "In init2 in EditUserInfoActivity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putBoolean("IsEditInfoActivity", true);

//        DataEntryFragment dataEntryFragment = new DataEntryFragment();
//        dataEntryFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame_container, DataEntryFragment.newInstance(user))
                .commit();
    }
//
//    private void init(User user) {
//        editTextName = findViewById(R.id.edit_text_name);
//        editTextNumber = findViewById(R.id.edit_text_contact_number);
//        editTextNumber2 = findViewById(R.id.edit_text_contact_number2);
//        editTextNumber3 = findViewById(R.id.edit_text_contact_number3);
//        addAnotherNumberButton = findViewById(R.id.add_another_mobile_button);
//        addAnotherNumberButton2 = findViewById(R.id.add_another_mobile_button2);
//        linearLayout2 = findViewById(R.id.linear_layout_2);
//        buttonSave = findViewById(R.id.button_save);
//        buttonImageSelect = findViewById(R.id.image_view_profile_pic);
//        textViewBirthday = findViewById(R.id.text_view_birthday);
//        imageView = findViewById(R.id.image_view_profile_pic);
//
//        setDataInEditTexts(user);
//
//        if (user.getProfilePic() == null) {
//            imageView.setImageResource(R.drawable.ic_baseline_person_24);
//            Glide.with(this)
//                    .load(R.drawable.ic_baseline_person_24)
//                    .placeholder(R.drawable.ic_baseline_person_24)
//                    .circleCrop()
//                    .into(imageView);
//        } else {
//            imageView.setImageURI(Uri.parse(user.getProfilePic()));
//            Glide.with(this)
//                    .load(Uri.parse(user.getProfilePic()))
//                    .placeholder(R.drawable.ic_baseline_person_24)
//                    .circleCrop()
//                    .into(imageView);
//        }
//        textViewBirthday.setOnClickListener(this);
//        buttonSave.setOnClickListener(this);
//        buttonImageSelect.setOnClickListener(this);
//        addAnotherNumberButton.setOnClickListener(this);
//        addAnotherNumberButton2.setOnClickListener(this);
//    }
//
//    private void setDataInEditTexts(User user) {
//        Log.d("abc", "Here in set data edit texts: " + user.getContactNumbers().get(0));
//        editTextName.getEditText().setText(user.getName());
//        dateInMilliSeconds = user.getDateOfBirth();
//        String birthDate = HelperFunctions.getDay(user.getDateOfBirth())+"/"
//                +HelperFunctions.getMonth(user.getDateOfBirth())+"/"
//                +HelperFunctions.getYear(user.getDateOfBirth());
//        textViewBirthday.setText("Date of Birth: " + birthDate);
//        if (user.getContactNumbers().size() == 1) {
//            editTextNumber.getEditText().setText(user.getContactNumbers().get(0));
//            linearLayout2.setVisibility(View.GONE);
//            editTextNumber3.setVisibility(View.GONE);
//        } else if (user.getContactNumbers().size() == 2) {
//            //Log.d("abc","In setDataInEditTexts: ");
//            editTextNumber.getEditText().setText(user.getContactNumbers().get(0));
//            editTextNumber2.getEditText().setText(user.getContactNumbers().get(1));
//            editTextNumber3.setVisibility(View.GONE);
//        } else {
//            editTextNumber.getEditText().setText(user.getContactNumbers().get(0));
//            editTextNumber2.getEditText().setText(user.getContactNumbers().get(1));
//            editTextNumber3.getEditText().setText(user.getContactNumbers().get(2));
//        }
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.text_view_birthday:
//                buttonDatePickerClicked();
//                break;
//            case R.id.button_save:
//                saveButtonClicked();
//                break;
//            case R.id.image_view_profile_pic:
//                buttonSelectProfilePicClicked();
//                break;
//            case R.id.add_another_mobile_button:
//                linearLayout2.setVisibility(View.VISIBLE);
//                break;
//            case R.id.add_another_mobile_button2:
//                editTextNumber3.setVisibility(View.VISIBLE);
//                break;
//        }
//    }
//
//    private void buttonSelectProfilePicClicked() {
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Choose your profile picture");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int item) {
//                if (options[item].equals("Take Photo")) {
//                    checkPermissionAndStartCamera();
//                } else if (options[item].equals("Choose from Gallery")) {
//                    checkPermissionAndOpenGallery();
//                } else
//                    dialogInterface.dismiss();
//            }
//        });
//        builder.show();
//    }
//
//    private void checkPermissionAndOpenGallery() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
//                PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
//                        PackageManager.PERMISSION_GRANTED)
//
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_CODE_GALLERY);
//        else
//            startOpenGalleryIntent();
//    }
//
//    private void startOpenGalleryIntent() {
//        Intent intentPickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(intentPickPhoto, REQUEST_CODE_GALLERY);
//    }
//
//    private void checkPermissionAndStartCamera() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
//                PackageManager.PERMISSION_GRANTED)
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
//        else
//            startTakePictureIntent();
//    }
//
//    private void startTakePictureIntent() {
//        Intent intentTakePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intentTakePicture, REQUEST_CODE_CAMERA);
//    }
//
//    private void buttonDatePickerClicked() {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
//            month = month + 1; // As Jan starts from 0
//            String date = "Date of Birth:" + dayOfMonth + "/" + month + "/" + year;
//            textViewBirthday.setText(date);
//            dateInMilliSeconds = HelperFunctions.getDateinMilli(dayOfMonth, month-1, year);
//        },
//                Calendar.getInstance().get(Calendar.YEAR),
//                Calendar.getInstance().get(Calendar.MONTH),
//                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case REQUEST_CODE_CAMERA:
//                    Bitmap bitmapCameraImage = (Bitmap) data.getExtras().get("data");
//                    Uri cameraImageUri = null;
//                    try {
//                        Log.d("TAG", "Inside try of onActivity result of DataEntryFragment");
//                        cameraImageUri = SaveBitmap.saveBitmapReturnUri(bitmapCameraImage);
//                        Log.d("TAG", "cmeraUri: " + cameraImageUri.toString());
//                        Log.d("TAG", "cameraUri: " + cameraImageUri.getPath());
//
//                    } catch (IOException e) {
//                        Log.d("TAG", "Inside catch: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//                    imageView.setImageURI(cameraImageUri);
//                    Glide.with(this)
//                            .load(cameraImageUri)
//                            .placeholder(R.drawable.ic_baseline_person_24)
//                            .circleCrop()
//                            .into(imageView);
//
//                    ProfilePicPath = cameraImageUri.toString();
//                    break;
//
//                case REQUEST_CODE_GALLERY:
//                    Uri selectedImageUri = data.getData();
//                    Log.d("TAG", "URi: " + selectedImageUri.getPath());
//                    ProfilePicPath = selectedImageUri.toString();
//
//                    imageView.setImageURI(selectedImageUri);
//                    Glide.with(this)
//                            .load(selectedImageUri)
//                            .placeholder(R.drawable.ic_baseline_person_24)
//                            .circleCrop()
//                            .into(imageView);
//                    break;
//            }
//        }
//    }
//
//    private void saveButtonClicked() {
//        String userName = editTextName.getEditText().getText().toString();
//        String contactNumber = editTextNumber.getEditText().getText().toString();
//        String contactNumber2 = editTextNumber2.getEditText().getText().toString();
//        String contactNumber3 = editTextNumber3.getEditText().getText().toString();
//        ArrayList<String> contactNumbers = new ArrayList<>();
//        contactNumbers.add(contactNumber);
//
//        if (!contactNumber2.equals("")) contactNumbers.add(contactNumber2);
//        if (!contactNumber3.equals("")) contactNumbers.add(contactNumber3);
//        if (!contactNumber.equals("") && contactNumber.charAt(0) != '+' && contactNumber.length() == 10)
//            contactNumber = "+91" + contactNumber;
//        if (!contactNumber2.equals("") && contactNumber2.charAt(0) != '+' && contactNumber2.length() == 10)
//            contactNumber2 = "+91" + contactNumber2;
//        if (!contactNumber3.equals("") && contactNumber3.charAt(0) != '+' && contactNumber3.length() == 10)
//            contactNumber3 = "+91" + contactNumber3;
//
//        if (userName.equals("") || contactNumber.equals("")) {
//            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!checkMobileNumberConstraints(contactNumber, contactNumber2, contactNumber3)) {
//            Toast.makeText(this, "Enter Valid numbers", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String birthDateString = textViewBirthday.getText().toString();
//        int indexOfColon = birthDateString.indexOf(":");
//        String birthDate = birthDateString.substring(indexOfColon + 1);
//
//        long currentTime = System.currentTimeMillis();
//        Log.d("abc", "Here pfp: " + ProfilePicPath);
//
//        User userUpdated = new User(uid, userName, contactNumbers, ProfilePicPath, dateInMilliSeconds, currentTime);
//        dateInMilliSeconds = 0;
//        fragmentViewModel.updateUser(userUpdated, this);
//        ProfilePicPath = null;
//        onBackPressed();
//    }
//
//    private boolean checkMobileNumberConstraints(String contactNumber, String contactNumber2, String contactNumber3) {
//        if (!(contactNumber.equals("") || contactNumber.charAt(0) == '+' || contactNumber.length() <= 10))
//            return false;
//        if (!(contactNumber2.equals("") || contactNumber2.charAt(0) == '+' || contactNumber2.length() <= 10))
//            return false;
//        if (!(contactNumber3.equals("") || contactNumber3.charAt(0) == '+' || contactNumber3.length() <= 10))
//            return false;
//        return true;
//    }
}