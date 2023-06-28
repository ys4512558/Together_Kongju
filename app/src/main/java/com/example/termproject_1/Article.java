package com.example.termproject_1;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Article implements Serializable {

    private int index;
    private String categori;
    private String title;
    private String userID;
    private String date;
    private String content;
    private ArrayList<String> comments;
    private ArrayList<String> comment_writers;
    private String nickName;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCategori() {
        return categori;
    }

    public void setCategori(String categori) {
        this.categori = categori;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getComment_writers() {
        return comment_writers;
    }

    public void setComment_writer(ArrayList<String> comment_writers) {
        this.comment_writers = comment_writers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Article(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formatedNow = now.format(formatter);
        this.title = "제목";
        this.userID = "작성자";
        this.date = formatedNow;
    }

    public Article(int index, String categori, String title, String userID,
                   String date, String content,
                   ArrayList<String> comments, ArrayList<String> comment_writers, String nickName){
        this.index = index;
        this.categori = categori;
        this.title = title;
        this.userID = userID;
        this.date = date;
        this.content = content;
        this.comments = comments;
        this.comment_writers = comment_writers;
        this.nickName = nickName;
    }
}
