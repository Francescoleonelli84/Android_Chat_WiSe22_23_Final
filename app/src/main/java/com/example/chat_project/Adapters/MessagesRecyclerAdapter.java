package com.example.chat_project.Adapters;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_project.R;
import com.example.chat_project.Model.ChatMessage;

import java.util.List;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.MessageViewHolder>  {

    private List<ChatMessage> listChatMessages;

    public MessagesRecyclerAdapter(List<ChatMessage> listChatMessages) {
        this.listChatMessages = listChatMessages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_recycler, parent, false);

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessagesRecyclerAdapter.MessageViewHolder holder, int position) {
        holder.textViewTimestamp.setText(listChatMessages.get(position).getTime());
        holder.textViewSenderID.setText(String.valueOf(listChatMessages.get(position).getSender_id()));
        holder.textViewMessageContent.setText(String.valueOf(listChatMessages.get(position).getContent()));

    }

    @Override
    public int getItemCount() {
        Log.v(MessagesRecyclerAdapter.class.getSimpleName(),""+listChatMessages.size());
        return listChatMessages.size();
    }


    /**
     * ViewHolder class
     */
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewTimestamp;
        public AppCompatTextView textViewSenderID;
        public AppCompatTextView textViewMessageContent;

        public MessageViewHolder(View view) {
            super(view);
            textViewTimestamp = (AppCompatTextView) view.findViewById(R.id.textViewTimestamp);
            textViewSenderID = (AppCompatTextView) view.findViewById(R.id.textViewSenderID);
            textViewMessageContent = (AppCompatTextView) view.findViewById(R.id.textViewMessageContent);
        }
    }

}
