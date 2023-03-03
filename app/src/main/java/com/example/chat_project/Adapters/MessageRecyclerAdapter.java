package com.example.chat_project.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_project.Model.User;
import com.example.chat_project.R;

import java.util.List;

public class MessageRecyclerAdapater extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder>{

    private List<User> listUsers;

    @Override
    public UsersRecyclerAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view -> multiplicating views
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new UsersRecyclerAdapter.UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRecyclerAdapter.UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
