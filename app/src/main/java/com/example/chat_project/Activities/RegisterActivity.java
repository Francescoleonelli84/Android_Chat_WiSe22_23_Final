package com.example.chat_project.Activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.example.chat_project.Helpers.InputValidation;
import com.example.chat_project.Model.User;
import com.example.chat_project.R;
import com.example.chat_project.Sql.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;
    private static final int PICK_IMAGE_REQUEST = 100;
    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private InputValidation inputValidation;
    private AppCompatButton appCompatButtonUploadImage;
    private AppCompatButton appCompatButtonRegister;
    private AppCompatButton appCompatButtonVerifyPIN;
    private AppCompatImageView appCompatImageViewProfileImage;
    private AppCompatTextView appCompatTextViewLoginLink;
    private DatabaseHelper databaseHelper;
    private ProfileActivity profileActivity;
    private User user;
    private Uri imagePath;
    private Bitmap imageToStore;
    static String pin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");

        initViews();
        initListeners();
        initObjects();
        setupUI(findViewById(R.id.parent));
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonUploadImage = (AppCompatButton) findViewById(R.id.appCompatButtonUploadImage);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        appCompatButtonVerifyPIN = (AppCompatButton) findViewById(R.id.appCompatButtonVerifyPin);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
        appCompatImageViewProfileImage = (AppCompatImageView) findViewById(R.id.appCompatImageViewProfileImageReg);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatButtonUploadImage.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
        appCompatButtonVerifyPIN.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonUploadImage:
                uploadImage();
                break;

            case R.id.appCompatButtonVerifyPin:
                enableButtons(appCompatButtonRegister, textInputLayoutPassword, textInputLayoutConfirmPassword);
                break;

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                break;
        }
    }

    /**
     * method to pick Image from gallery and display it in imageview
     */
    public void uploadImage(){
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
                appCompatImageViewProfileImage.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Generates pin for the first login
     */

    static String randGeneratedStr(int l) {

        String AlphaNumericStr = "abcdefghijklmnopqrstuvxyz12345#*";

        StringBuilder s = new StringBuilder(l);
        int i;
        for (i = 0; i < l; i++) {
            int ch = (int) (AlphaNumericStr.length() * Math.random());
            pin = String.valueOf(s.append(AlphaNumericStr.charAt(ch)));
        }

        return pin;
    }
    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {


        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.comparePassword(textInputEditTextPassword, pin,
                textInputLayoutPassword, getString(R.string.error_password_matching))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }


        if (!databaseHelper.checkRegisteredUser(textInputEditTextEmail.getText().toString().trim())) {
            Log.d("test", "Button works");
            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());
            user.setImage(imageToStore);
            databaseHelper.addUser(user);
            appCompatButtonVerifyPIN.setEnabled(false);

            // Snack Bar to show success message that record saved successfully
            // Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show();
            emptyInputEditText();

        } else {
            // Snack Bar to show error message that record already exists
            Toast.makeText(this, "Error! E-Mail already registered!", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * This method is to enable Register-Button, Password and Confirm Password Inputfields
     * after sending password per E-Mail
     */
    public void enableButtons(AppCompatButton r, TextInputLayout p, TextInputLayout pc) {
        r.setEnabled(true);
        p.setEnabled(true);
        pc.setEnabled(true);
        sendVerificationEMail();
        Toast.makeText(this, "An Email with your password has been sent to " +
                textInputEditTextEmail.getText().toString().trim(), Toast.LENGTH_SHORT).show();
    }


    /**
     * method to send random code for the sign-up
     */
    public void sendVerificationEMail() {

        try {
            String stringSenderEmail = "chat.project.2023@gmail.com";

            String name = textInputEditTextName.getText().toString().trim();
            String stringReceiverEmail = textInputEditTextEmail.getText().toString().trim();

            // Password für Windows Systemen
            String stringPasswordSenderEmail = "ssyf cium irdi dhhd";

            // Password für MAC-Systemen
            //String stringPasswordSenderEmail = "vwnj zwoh mthg aesv";


            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            try {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            String geheimCode = randGeneratedStr(5);
            mimeMessage.setSubject("Subject: Android App email");
            mimeMessage.setText("Hallo " + name + "," + "\n\n Das ist Ihr Password" +
                    " für das erste Login:" + "\n\n" + geheimCode +
                    "\n\nSie können nach dem ersten Login Ihr password aktualisieren!\n\n" +
                    " Ihr Android Chat Project Team");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
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
                    hideSoftKeyboard(RegisterActivity.this);
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

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}
