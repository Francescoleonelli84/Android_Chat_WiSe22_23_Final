package com.example.chat_project.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.example.chat_project.Helpers.InputValidation;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chat_project.Model.User;
import com.example.chat_project.R;
import com.example.chat_project.Sql.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int PICK_IMAGE_REQUEST = 100;
    private final AppCompatActivity activity = ProfileActivity.this;
    private AppCompatEditText inputName;
    private AppCompatEditText inputAge;
    private AppCompatEditText inputCity;
    private AppCompatEditText inputSex;
    private AppCompatEditText inputNewPassword;
    private AppCompatImageView profileImage;
    private AppCompatButton saveProfile;
    private AppCompatButton changeImage;
    private AppCompatButton saveImage;
    private AppCompatTextView appCompatTextViewUsersListLink;
    private DatabaseHelper databaseHelper;
    private User user;
    private String email;
    private Uri imagePath;
    private Bitmap imageToStore;
    private InputValidation inputValidation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");
        setupUI(findViewById(R.id.constraintLayout));

        initViews();
        initListeners();
        initObjects();

        email= getIntent().getStringExtra("key_email");
        getUserDetails();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        inputName = (AppCompatEditText) findViewById(R.id.editTextUserName);
        inputAge =   (AppCompatEditText)findViewById(R.id.editTextUserAge);
        inputCity=  (AppCompatEditText)findViewById(R.id.editTextCity);
        inputSex = (AppCompatEditText)findViewById(R.id.editTextUserSex);
        inputNewPassword = (AppCompatEditText) findViewById(R.id.editTextPassword);
        profileImage = (AppCompatImageView)findViewById(R.id.imageViewProfileImage);
        saveProfile = (AppCompatButton) findViewById(R.id.buttonSaveProfile);
        changeImage = (AppCompatButton) findViewById(R.id.buttonChangeImage);
        saveImage = (AppCompatButton) findViewById(R.id.buttonSaveImage);
        appCompatTextViewUsersListLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewUsersListLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {

        changeImage.setOnClickListener(this);
        saveImage.setOnClickListener(this);
        saveProfile.setOnClickListener(this);
        appCompatTextViewUsersListLink.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {

        databaseHelper = new DatabaseHelper(activity);
        user = new User();
    }

    /**
     * This method is to display profile data
     */
    public void getUserDetails(){

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<User> al= databaseHelper.getLoggedinUserDetails(email);
        User user = al.get(0);

        inputName.setText(user.getName());
        inputCity.setText(user.getCity());
        inputAge.setText(String.valueOf(user.getAge()));
        inputSex.setText(user.getSex());
        inputNewPassword.setText(user.getPassword());
        profileImage.setImageBitmap(user.getImage());

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonChangeImage:
                selectAnImage();
                break;
            case R.id.buttonSaveImage:
                updateImage();
                break;
            case R.id.buttonSaveProfile:
                postDataToSQLite();
                break;
            case R.id.appCompatTextViewUsersListLink:
                // Navigate to UsersListActivity
                Intent intentUsersList = new Intent(getApplicationContext(), UsersListActivity.class);
                intentUsersList.putExtra("key_email",email);
                startActivity(intentUsersList);
                break;
        }
    }


    /**
     * method to pick Image from gallery and display it in imageview
     * Sources:
     * 1) https://www.youtube.com/watch?v=H1ja8gvTtBE
     * 2) https://www.youtube.com/watch?v=8_LuejJEF7o
     */
    public void selectAnImage(){
        //Log.d("test", "Button works");
        //Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        //startActivityForResult(intent, 3);
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && data != null) {
                imagePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profileImage.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is to update profile and post data to the database
     */
    private void updateImage() {

        user.setImage(imageToStore);
        databaseHelper.updateUserImage(user);
        Toast.makeText(this, "Profile Image successfully updated", Toast.LENGTH_SHORT).show();

    }
    /**
     * This method is to update profile and post data to the database
     */
    private void postDataToSQLite() {

        int i = Integer.parseInt(String.valueOf(inputAge.getText()));

        if (i <= 0) {
            Toast.makeText(this, "Error! You must input your age!", Toast.LENGTH_SHORT).show();
            return;

        }
        else if(i < 18){
            Toast.makeText(this, "Error! You must be older than 18 in order to use the app!", Toast.LENGTH_SHORT).show();
            return;
        }

        else {

            user.setName(inputName.getText().toString().trim());
            user.setAge(Integer.parseInt(inputAge.getText().toString().trim()));
            user.setSex(inputSex.getText().toString().trim());
            user.setCity(inputCity.getText().toString().trim());
            user.setPassword(inputNewPassword.getText().toString().trim());
            databaseHelper.updateUser(user);
            //set counter to +1 to remember he modified his profile

            Toast.makeText(this, "Profile successfully saved", Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    /**
     * Method to hide the  keyboard when touching the screen
     */
    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ProfileActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}