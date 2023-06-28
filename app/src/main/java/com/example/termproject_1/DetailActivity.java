package com.example.termproject_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity {

    TextView title_detail, content_detail, date_detail, name_detail;
    ListView comment_view;
    EditText Edit_comment;
    Button commentBtn;

    Article article_detail;
    Comment_Adapter adapter;
    String userID, nickName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        userID = getIntent().getStringExtra("userID");
        nickName = getIntent().getStringExtra("nickName");

        comment_view = (ListView) findViewById(R.id.comment_list);
        adapter = new Comment_Adapter();

        article_detail = new Article();
        article_detail = (Article) getIntent().getSerializableExtra("article_detail");

        title_detail = findViewById(R.id.title_detail);
        name_detail = findViewById(R.id.name_detail);
        date_detail = findViewById(R.id.date_detail);
        content_detail = findViewById(R.id.content_detail);

        String temp [] = article_detail.getDate().split("\n");

        title_detail.setText(article_detail.getTitle());
        date_detail.setText(temp[0] + " " + temp[1]);
        name_detail.setText(article_detail.getNickName());
        content_detail.setText(article_detail.getContent());

        /*for(int i = 0; i < article_detail.getComment_writers().size(); i++){
            if(!(article_detail.getComment_writers().get(i).equals("null"))){
                adapter.Add_comment(article_detail.getComments().get(i),
                        article_detail.getComment_writers().get(i));
            }
        }

        comment_view.setAdapter(adapter);*/

        Comment_set();

        Edit_comment = (EditText) findViewById(R.id.Edit_comment);
        commentBtn = (Button) findViewById(R.id.commentBtn);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edComment = Edit_comment.getText().toString();

                ArrayList<String> newComments = new ArrayList<String>();
                newComments = article_detail.getComments();
                newComments.add(edComment);

                ArrayList<String> newWrite = new ArrayList<String>();
                newWrite = article_detail.getComment_writers();
                newWrite.add(nickName);
                Comment_set();

                String index = Integer.toString(article_detail.getIndex());

                try {
                    new Comment_Task().execute(index, edComment, nickName).get();
                    Edit_comment.setText("");
                    InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void Comment_set(){
        adapter = new Comment_Adapter();
        for(int i = 0; i < article_detail.getComment_writers().size(); i++){
            if(!(article_detail.getComment_writers().get(i).equals("null"))){
                adapter.Add_comment(article_detail.getComments().get(i),
                        article_detail.getComment_writers().get(i));
            }
        }
        comment_view.setAdapter(adapter);
    }
}

class Comment_Task extends AsyncTask<String ,Void, String>{

    public static String ip = "192.168.219.190:8080";
    String sendMsg, receiveMsg;
    String serverip = "http://"+ip+"/android/Comment_update.jsp";

    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL(serverip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            sendMsg = "index=" + strings[0]
                    + "&comment=" + strings[1]
                    + "&comment_writer=" + strings[2];

            osw.write(sendMsg);
            osw.flush();
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}