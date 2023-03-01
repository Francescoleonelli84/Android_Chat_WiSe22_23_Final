package com.example.chat_project.Model;

public class ChatMessage {
    private int message_id;
    private int sender_id;
    private int receiver_id;
    private String content;
    private String time;

    public int getMessage_id(){return message_id;}

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getSender_id() {
        return sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public ChatMessage( int message_id, int sender_id, int receiver_id,String content, String time) {

        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
        this.time = time;
    }

    public ChatMessage() {

    }

    public void setMessage_id(int message_id){this.message_id= message_id;}

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

}