package com.example.chat_project.Sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.chat_project.Model.ChatMessage;
import com.example.chat_project.Model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 18;
    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";
    // User table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_MESSAGE = "message";
    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_AGE = "user_age";
    private static final String COLUMN_USER_CITY = "user_city";
    private static final String COLUMN_USER_SEX = "user_sex";
    private static final String COLUMN_USER_IMAGE = "user_image";
    private static final String COLUMN_MESSAGE_ID = "message_id";
    private static final String COLUMN_SENDER_ID = "sender_id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_MESSAGE_TIME = "timestamp";
    private static final String COLUMN_RECEIVER_ID = "receiver_id";
    int rv = -1;
    public static int user_id;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    // create table user -> sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_AGE +
            " INTEGER," + COLUMN_USER_CITY + " TEXT," + COLUMN_USER_SEX + " TEXT," + COLUMN_USER_IMAGE + " BLOB" +")";
    // drop table user sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // create table message -> sql query
    private String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
            + COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SENDER_ID + " INTEGER, " + COLUMN_RECEIVER_ID + " INTEGER, "
            + COLUMN_CONTENT + " TEXT, " + COLUMN_MESSAGE_TIME + " TEXT, "
            + " FOREIGN KEY(" + COLUMN_SENDER_ID + ")REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "),"
            + " FOREIGN KEY(" + COLUMN_RECEIVER_ID + ")REFERENCES "  + TABLE_USER + "(" + COLUMN_USER_ID + ")" +")";

    private String DROP_MESSAGE_TABLE = "DROP TABLE IF EXISTS " + TABLE_MESSAGE;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User/Message Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_MESSAGE_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {

        Bitmap imageToStoreBitmap = user.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_AGE, 0);
        values.put(COLUMN_USER_CITY, "City Unknown");
        values.put(COLUMN_USER_SEX, "Gender Unknown");
        values.put(COLUMN_USER_IMAGE, imageInBytes);

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();

    }
    /**
     * This method is to create chatMessage record
     *
     * @param chatMessage
     */
    public void insertMessage(ChatMessage chatMessage){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTENT,chatMessage.getContent());
        values.put(COLUMN_MESSAGE_TIME,chatMessage.getTime());
        values.put(COLUMN_SENDER_ID,chatMessage.getSender_id());
        values.put(COLUMN_RECEIVER_ID,chatMessage.getReceiver_id());

        // Inserting Row
        db.insert(TABLE_MESSAGE,null,values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    @SuppressLint("Range")
    public List<User> getAllUser(String email) {

        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        String query = "SELECT * FROM user WHERE user_email !='" + email + "'";
        Cursor cursor = db.rawQuery(query,null);
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setAge(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_AGE))));
                user.setSex(cursor.getString(cursor.getColumnIndex(COLUMN_USER_SEX)));
                user.setCity(cursor.getString(cursor.getColumnIndex(COLUMN_USER_CITY)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }

    /**
     * This method is to fetch all messages and return the list of message records
     *
     * @return chatMessageList
     */
    @SuppressLint("Range")
    public List<ChatMessage> getAllChatMessages(int receiver_id) {

        List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM message WHERE receiver_id ='" + receiver_id + "'";
        Cursor cursor = db.rawQuery(query,null);
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSender_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID))));
                chatMessage.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TIME)));
                chatMessage.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                // Adding chatMessage record to list
                chatMessageList.add(chatMessage);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return chatMessage list
        return chatMessageList;
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + "= ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user(email) exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkRegisteredUser(String email) {
        // array of columns to fetch
        String[] id = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query user table with condition
        /**
         * SQL equivalent to this query is:
         * SELECT user_id FROM user WHERE user_email = 'user@email.de'
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                id,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    /**
     * This method to validate user email and password
     *
     * @param email
     * @param password
     * @return true/false
     */
    @SuppressLint("Range")
    public boolean checkLoggedUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        /**
         * SQL equivalent to this query is:
         * SELECT user_id FROM user WHERE user_email = 'user@email.de' AND user_password = '123password';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);
        if (cursor.moveToFirst()) {
            rv = cursor.getInt(cursor.getColumnIndex("user_id"));
            getId(rv);
        }
        //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public int getId(int i) {
        //stores index of columns
        user_id = i;
        return user_id;
    }

    /**
     * This method is to get single user data from database and return
     */
    public ArrayList<User> getLoggedinUserDetails(String email){

        ArrayList<User> al = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM user WHERE user_email ='" + email + "'";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String password = cursor.getString(3);
            int age = cursor.getInt(4);
            String city = cursor.getString(5);
            String sex = cursor.getString(6);
            byte [] imageByte = cursor.getBlob(7);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);

            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPassword(password);
            user.setAge(age);
            user.setCity(city);
            user.setSex(sex);
            user.setImage(bitmap);

            al.add(user);

        }
        return al;
    }

    /**
     * This method to update user's profile image
     *
     * @param user
     */
    public void updateUserImage(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        Bitmap imageToStoreBitmap = user.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_IMAGE, imageInBytes);
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user_id)});
        db.close();
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_CITY, user.getCity());
        values.put(COLUMN_USER_AGE, user.getAge());
        values.put(COLUMN_USER_SEX, user.getSex());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user_id)});
        db.close();

    }
}