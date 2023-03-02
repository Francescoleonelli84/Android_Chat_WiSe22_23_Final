package com.example.chat_project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import 	androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chat_project.R;
import com.example.chat_project.Adapters.UsersRecyclerAdapter;
import com.example.chat_project.Model.User;
import com.example.chat_project.Sql.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private AppCompatImageView imageView;
    private RecyclerView recyclerViewUsers;
    private AppCompatButton btnOpenChat;
    private List<User> listUsers;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    private AppCompatEditText inputUserID;
    private String email;
    private String receiver_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().setTitle("Chat_Project");
        initViews();
        initObjects();
        initListeners();
        email= getIntent().getStringExtra("key_email");
        getUserDetail();
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        imageView = (AppCompatImageView)findViewById(R.id.imageViewImage);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        inputUserID = (AppCompatEditText) findViewById(R.id.editTextUserID);
        btnOpenChat = (AppCompatButton) findViewById(R.id.btnOpenChat);

    }
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);
        getDataFromSQLite();
    }

    private void initListeners(){
       btnOpenChat.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOpenChat:
                receiver_id = inputUserID.getText().toString().trim();
                Intent intentChatting = new Intent(getApplicationContext(), ChatActivity.class);
                intentChatting.putExtra("key_email",email);
                intentChatting.putExtra("key_receiver_id", receiver_id);
                emptyInputEditText();
                startActivity(intentChatting);
                break;
            case R.id.appCompatButtonRegister:
                // Navigate to Chat Activity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                //intentChatting.putExtra("key_email",email);
                startActivity(intentRegister);
                break;
        }

    }

    /**
     * This method is to fetch username and display username in textview
     */
    public void getUserDetail(){

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<User> al= databaseHelper.getLoggedinUserDetails(email);
        User user = al.get(0);

        textViewName.setText("Hello, "+user.getName());
        imageView.setImageBitmap(user.getImage());
    }


    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void emptyInputEditText() {
        inputUserID.setText(null);
    }
}