package com.example.chat_project.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import 	androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chat_project.Adapters.MessagesRecyclerAdapter;
import com.example.chat_project.Adapters.UsersRecyclerAdapter;
import com.example.chat_project.Model.ChatMessage;
import com.example.chat_project.Model.User;
import com.example.chat_project.R;
import com.example.chat_project.Sql.DatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = ChatActivity.this;
    private DatabaseHelper databaseHelper;
    private AppCompatButton appCompatButtonSend;
    private AppCompatButton appCompatButtonReceive;
    private AppCompatEditText textInputMessageContent;
    private AppCompatTextView textOutputMessageContent;
    private AppCompatTextView textViewChatWith;
    private RecyclerView recyclerViewMessages;
    private List<ChatMessage> listChatMessages;
    private MessagesRecyclerAdapter messagesRecyclerAdapter;
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

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textInputMessageContent = (AppCompatEditText) findViewById(R.id.textInputMessageContent);
        appCompatButtonSend = (AppCompatButton) findViewById(R.id.btnSend);
        appCompatButtonReceive = (AppCompatButton) findViewById(R.id.btnReceive);
        textOutputMessageContent = (AppCompatTextView) findViewById(R.id.textOutputMessageContent);
        textViewChatWith = (AppCompatTextView) findViewById(R.id.textViewChatWith);
        recyclerViewMessages = (RecyclerView) findViewById(R.id.recyclerViewMessages);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonSend.setOnClickListener(this);
        appCompatButtonReceive.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listChatMessages = new ArrayList<>();
        messagesRecyclerAdapter = new MessagesRecyclerAdapter(listChatMessages);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMessages.setLayoutManager(mLayoutManager);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setAdapter(messagesRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);
        chatMessage = new ChatMessage();
        user = new User();
    }

    /**
     * This method is to get the logined sender_id
     */
    private int getSenderID(){

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<User> al= databaseHelper.getLoggedinUserDetails(email);
        User user = al.get(0);
        id = user.getId();
        return id;
    }

    /**
     * This method is to send message to the person with the ID which was given
     */
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

            Toast.makeText(this, "Message is sent successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is to get the date formatted
     */
    public String getRequiredTime(String timeStamp){
        try{
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date netDate = (new Date(Long.parseLong(timeStamp)));
            return sdf.format(netDate);
        } catch (Exception ignored) {
            return "xx";
        }
    }

    /**
     * This method is to fetch all message records from SQLite
     */
    private void getDataFromSQLite() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                receiver_id = sender_id;
                listChatMessages.clear();
                listChatMessages.addAll(databaseHelper.getAllChatMessages(receiver_id));
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                messagesRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    /**
     * This method is to empty input edit text
     */
    private void emptyInputEditText() {
        textInputMessageContent.setText(null);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
                sendMessage();
                break;
            case R.id.btnReceive:
                getDataFromSQLite();
                break;
        }
    }
}