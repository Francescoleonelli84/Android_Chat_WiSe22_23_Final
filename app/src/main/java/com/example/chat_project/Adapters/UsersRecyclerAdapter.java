package com.example.chat_project.Adapters;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_project.R;
import com.example.chat_project.Model.User;

import java.util.List;


public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<User> listUsers;

    public UsersRecyclerAdapter(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view -> multiplicating views
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewName.setText(listUsers.get(position).getName());
        holder.textViewSex.setText(listUsers.get(position).getSex());
        holder.textViewId.setText(String.valueOf(listUsers.get(position).getId()));
        holder.textViewAge.setText(String.valueOf(listUsers.get(position).getAge()));
        holder.textViewCity.setText(listUsers.get(position).getCity());

    }

    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(),""+listUsers.size());
        return listUsers.size();
    }


    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewName;
        public AppCompatTextView textViewSex;
        public AppCompatTextView textViewId;
        public AppCompatTextView textViewCity;
        public AppCompatTextView textViewAge;


        public UserViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewId = (AppCompatTextView) view.findViewById(R.id.textViewId);
            textViewAge = (AppCompatTextView) view.findViewById(R.id.textViewAge);
            textViewSex = (AppCompatTextView) view.findViewById(R.id.textViewSex);
            textViewCity = (AppCompatTextView) view.findViewById(R.id.textViewCity);
        }
    }


}
