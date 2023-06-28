package com.example.termproject_1;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;


public class Chat {

    private String title;
    private int port;
    private ArrayList<String> nickname;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Chat(){
        this.title = "제목";
    }

    public Chat(String title, int port, ArrayList<String> nickname){
        this.title = title;
        this.port = port;
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ArrayList<String> getUserIDs() {
        return nickname;
    }

    public void setUserIDs(ArrayList<String> nickname) {
        this.nickname = nickname;
    }
}