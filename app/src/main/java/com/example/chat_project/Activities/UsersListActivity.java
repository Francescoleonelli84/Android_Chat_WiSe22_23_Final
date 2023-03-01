package com.example.chat_project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import 	androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chat_project.R;
import com.example.chat_project.Adapters.UsersRecyclerAdapter;
import com.example.chat_project.Model.User;
import com.example.chat_project.Sql.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private AppCompatImageView imageView;
    private AppCompatButton buttonChat;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    private String email;

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
        buttonChat = (AppCompatButton) findViewById(R.id.buttonChat);
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
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
        getDataFromSQLite();
    }

    private void initListeners(){
       buttonChat.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonChat:
                Intent intentChatting = new Intent(getApplicationContext(), ChatActivity.class);
                intentChatting.putExtra("key_email",email);
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
}