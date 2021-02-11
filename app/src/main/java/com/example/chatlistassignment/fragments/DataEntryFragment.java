package com.example.chatlistassignment.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.chatlistassignment.R;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.utils.HelperFunctions;
import com.example.chatlistassignment.utils.SaveBitmap;
import com.example.chatlistassignment.viewmodel.FragmentViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;

public class DataEntryFragment extends Fragment implements View.OnClickListener {

    TextInputLayout editTextUserName, editTextContactNumber, editTextContactNumber2, editTextContactNumber3;
    Button buttonSave;
    ImageButton buttonAnotherEditText, buttonAnotherEditText2;
    LinearLayout linearLayout2;
    TextInputEditText editTextDatePicker;
    String ProfilePicPath;
    ImageView imageViewProfilePic, buttonSelectProfilePic;
    FragmentViewModel fragmentViewModel;
    long dateEnteredinMilli = 0;

    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentViewModel = ViewModelProviders.of(this).get(FragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_entry, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        editTextUserName = view.findViewById(R.id.edit_text_username);
        editTextContactNumber = view.findViewById(R.id.edit_text_contact_number);
        editTextContactNumber2 = view.findViewById(R.id.edit_text_contact_number2);
        editTextContactNumber3 = view.findViewById(R.id.edit_text_contact_number3);
        editTextContactNumber3.setVisibility(View.GONE);
        linearLayout2 = view.findViewById(R.id.linear_layout_2);
        linearLayout2.setVisibility(View.GONE);
        buttonAnotherEditText = view.findViewById(R.id.add_another_mobile);
        buttonAnotherEditText2 = view.findViewById(R.id.add_another_mobile2);
        editTextDatePicker = view.findViewById(R.id.text_view_date_picker);
        editTextDatePicker.setFocusable(false);
        buttonSave = view.findViewById(R.id.button_save);
        buttonSelectProfilePic = view.findViewById(R.id.image_view_profile_pic);
        imageViewProfilePic = view.findViewById(R.id.image_view_profile_pic);

        Glide.with(getContext())
                .load(R.drawable.ic_baseline_person_24)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(imageViewProfilePic);

        editTextDatePicker.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonSelectProfilePic.setOnClickListener(this);
        buttonAnotherEditText.setOnClickListener(this);
        buttonAnotherEditText2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save:
                saveButtonClicked();
                break;
            case R.id.image_view_profile_pic:
                buttonSelectProfilePicClicked();
                break;
            case R.id.text_view_date_picker:
                buttonDatePickerClicked();
                break;
            case R.id.add_another_mobile:
                addSecondEditText();
                break;
            case R.id.add_another_mobile2:
                addThirdEditText();
                break;
        }
    }

    private void addThirdEditText() {
        editTextContactNumber3.setVisibility(View.VISIBLE);
    }

    private void addSecondEditText() {
        linearLayout2.setVisibility(View.VISIBLE);
    }

    private void buttonDatePickerClicked() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1; // As Jan starts from 0
                String date = dayOfMonth + "/" + month + "/" + year;
                editTextDatePicker.setText(date);
                dateEnteredinMilli = HelperFunctions.getDateinMilli(dayOfMonth, month-1, year);
                Log.d("abc", "in datepickerclicked: "+dateEnteredinMilli);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void buttonSelectProfilePicClicked() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
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

    private void startTakePictureIntent() {
        Intent intentTakePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePicture, REQUEST_CODE_CAMERA);
    }

    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        else
            startTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startTakePictureIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    Bitmap bitmapCameraImage = (Bitmap) data.getExtras().get("data");
                    Uri cameraImageUri = null;
                    try {
                        Log.d("TAG", "Inside try of onActivity result of DataEntryFragment");
                        cameraImageUri = SaveBitmap.saveBitmapReturnUri(bitmapCameraImage);
                        Log.d("TAG", "cmeraUri: " + cameraImageUri.toString());

                    } catch (IOException e) {
                        Log.d("TAG", "Inside catch: " + e.getMessage());
                        e.printStackTrace();
                    }
                    imageViewProfilePic.setImageURI(cameraImageUri);
                    Glide.with(getContext())
                            .load(cameraImageUri)
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .circleCrop()
                            .into(imageViewProfilePic);
                    ProfilePicPath = cameraImageUri.toString();
                    break;

                case REQUEST_CODE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    Log.d("TAG", "URi: " + selectedImageUri.getPath());
                    ProfilePicPath = selectedImageUri.toString();
                    Glide.with(getContext())
                            .load(selectedImageUri.toString())
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .circleCrop()
                            .into(imageViewProfilePic);

                    imageViewProfilePic.setImageURI(selectedImageUri);
                    break;
            }
        }
    }


    private void saveButtonClicked() {
        String userName = editTextUserName.getEditText().getText().toString();
        String contactNumber = editTextContactNumber.getEditText().getText().toString();
        if(HelperFunctions.checkNumber(contactNumber) == "Invalid" || contactNumber.equals("")){
            Toast.makeText(getContext(), "Enter Valid Mobile Numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        String contactNumber2 = editTextContactNumber2.getEditText().getText().toString();
        if(!(contactNumber2.equals("") || HelperFunctions.checkNumber(contactNumber2) != "Invalid")){
            Toast.makeText(getContext(), "Enter Valid Mobile Numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        String contactNumber3 = editTextContactNumber3.getEditText().getText().toString();
        if(!(contactNumber3.equals("") || HelperFunctions.checkNumber(contactNumber3) != "Invalid")){
            Toast.makeText(getContext(), "Enter Valid Mobile Numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> contactNumbers = new ArrayList<>();
        contactNumbers.add(HelperFunctions.checkNumber(contactNumber));
        if(!contactNumber2.equals("")) contactNumbers.add(HelperFunctions.checkNumber(contactNumber2));
        if(!contactNumber3.equals("")) contactNumbers.add(HelperFunctions.checkNumber(contactNumber3));

        if (userName.equals("") || contactNumber.equals("") || dateEnteredinMilli==0) {
            Toast.makeText(getContext(), "Please enter all the details", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkMobileNumberConstraints(contactNumber, contactNumber2, contactNumber3)){
            Toast.makeText(getContext(), "Enter Valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        long currentTime= System.currentTimeMillis();

        User user = new User(userName, contactNumbers, ProfilePicPath, dateEnteredinMilli, currentTime, currentTime);
        ProfilePicPath = null;
        fragmentViewModel.addUser(user, getContext());
        dateEnteredinMilli = 0;

        clearInputFields();
        changeTabChatList();

    }

    private boolean checkMobileNumberConstraints(String contactNumber, String contactNumber2, String contactNumber3) {
        if(!(contactNumber.equals("") || contactNumber.charAt(0)=='+' || contactNumber.length()<=10))return false;
        if(!(contactNumber2.equals("") || contactNumber2.charAt(0)=='+' || contactNumber2.length()<=10))return false;
        if(!(contactNumber3.equals("") || contactNumber3.charAt(0)=='+' || contactNumber3.length()<=10))return false;
        return true;
    }

    private void changeTabChatList() {
        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
        viewPager.setCurrentItem(0, true);
    }

    private void clearInputFields() {
        editTextUserName.getEditText().setText("");
        editTextContactNumber.getEditText().setText("");
        editTextContactNumber2.getEditText().setText("");
        editTextContactNumber3.getEditText().setText("");
        editTextDatePicker.setText("");
        linearLayout2.setVisibility(View.GONE);
        editTextContactNumber3.setVisibility(View.GONE);
        editTextDatePicker.setText("");
        imageViewProfilePic.setImageResource(R.drawable.ic_baseline_person_24);

        dateEnteredinMilli = 0;
    }
}