package com.example.chat_project.Model;

import android.graphics.Bitmap;

import com.example.chat_project.Sql.DatabaseHelper;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private Integer age;
    private String city;
    private String sex;
    private Bitmap image;
    private static int userloginCounter;

    public User(){

    }

    public User(int id, String name, String email, String password, Integer age, String city,
                String sex, Bitmap image, int userLoginCounter) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.city = city;
        this.sex = sex;
        this.image = image;
        this.userloginCounter = userLoginCounter;
    }



    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getId() { return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public static int getUserloginCounter() {
        return userloginCounter;
    }

    public void increment(){
        userloginCounter++;
    }


}