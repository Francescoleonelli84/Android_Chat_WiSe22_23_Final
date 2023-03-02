package com.example.chat_project.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chat_project.Model.ChatMessage;
import com.example.chat_project.Model.User;
import com.example.chat_project.R;
import com.example.chat_project.Sql.DatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = ChatActivity.this;
    private DatabaseHelper databaseHelper;
    private LinearLayout linearLayout;
    private AppCompatButton appCompatButtonSend;
    private AppCompatButton appCompatButtonReceive;
    private AppCompatEditText textInputMessageContent;
    private AppCompatTextView textOutputMessageContent;
    private AppCompatTextView textDisplayMessageContent;
    private AppCompatTextView textViewChatWith;
    private User user;
    private ChatMessage chatMessage;
    private String time;
    private String email;
    private int id;
    private String receiver;
    private int receiver_id;
    private int sender_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        initListeners();
        initObjects();
        email= getIntent().getStringExtra("key_email");
        sender_id = getSenderID();
        receiver = getIntent().getStringExtra("key_receiver_id");
        textViewChatWith.setText("You're chatting with Person_ID = "+ receiver);
        Log. d("test", "sender_id is " + sender_id);
        Log. d("test", "receiver_id is " + receiver);

    }

    private void initViews() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        textInputMessageContent = (AppCompatEditText) findViewById(R.id.textInputMessageContent);
        appCompatButtonSend = (AppCompatButton) findViewById(R.id.btnSend);
        appCompatButtonReceive = (AppCompatButton) findViewById(R.id.btnReceive);
        textOutputMessageContent = (AppCompatTextView) findViewById(R.id.textOutputMessageContent);
        textDisplayMessageContent = (AppCompatTextView) findViewById(R.id.textDisplayMessageContent);
        textViewChatWith = (AppCompatTextView) findViewById(R.id.textViewChatWith);
    }

    private void initListeners() {
        appCompatButtonSend.setOnClickListener(this);
        appCompatButtonReceive.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
        chatMessage = new ChatMessage();

    }

    private int getSenderID(){

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<User> al= databaseHelper.getLoggedinUserDetails(email);
        User user = al.get(0);
        id = user.getId();
        return id;

    }

    private void sendMessage() {
        if(textInputMessageContent.getText().toString().isEmpty() || textInputMessageContent.getText().toString().length() == 0){}
        else {
            Long tsLong = System.currentTimeMillis();
            time = getRequiredTime(tsLong.toString());
            chatMessage.setContent(textInputMessageContent.getText().toString());
            chatMessage.setTime(time);
            chatMessage.setSender_id(sender_id);
            chatMessage.setReceiver_id(Integer.valueOf(receiver));
            databaseHelper.insertMessage(chatMessage);

            textOutputMessageContent.setText(time + " "+ "Send to "+ receiver + "\n" +textInputMessageContent.getText().toString());
            emptyInputEditText();

            Toast.makeText(this, "Button works", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * here need to be fixed!!!!!!!!
     */
    private void ReceiveMessage() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        receiver_id = sender_id;
        ArrayList<ChatMessage> al= databaseHelper.getMessage(receiver_id);
        ChatMessage chatMessage = al.get(0);
        textDisplayMessageContent.setText(chatMessage.getTime()+ " from " + chatMessage.getSender_id() +"\n" +chatMessage.getContent());

    }

    public String getRequiredTime(String timeStamp){
        try{
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date netDate = (new Date(Long.parseLong(timeStamp)));
            return sdf.format(netDate);
        } catch (Exception ignored) {
            return "xx";
        }
    }

    private void emptyInputEditText() {
        textInputMessageContent.setText(null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
                sendMessage();
                break;
            case R.id.btnReceive:
                ReceiveMessage();
                break;
        }
    }
}