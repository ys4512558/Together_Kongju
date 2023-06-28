package com.example.termproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class chat_invite extends AppCompatActivity {

    Button inviteBtn;
    ListView invite_list;
    EditText Edit_name;
    Article article_detail;
    ArrayList<String> items = new ArrayList<String>();
    ArrayAdapter adapters;
    String userID, nickName;
    ArrayList<String> invite_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_invite);

        adapters = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);
        userID = getIntent().getStringExtra("userID");
        nickName = getIntent().getStringExtra("nickName");
        article_detail = (Article) getIntent().getSerializableExtra("article_detail");

        invite_list = (ListView) findViewById(R.id.invite_list) ;
        invite_list.setAdapter(adapters) ;

        for(int i = 0; i < article_detail.getComment_writers().size(); i++){
            items.add(article_detail.getComment_writers().get(i) + " : "
                    + article_detail.getComments().get(i));
            adapters.notifyDataSetChanged();
        }

        invite_user = new ArrayList<String>();

        inviteBtn = (Button) findViewById(R.id.inviteBtn);
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SparseBooleanArray check = invite_list.getCheckedItemPositions();
                int count = adapters.getCount();

                String users = "";
                if(check.size() != 0){
                    for(int i = 0 ; i < count; i++){
                        if(check.get(i) && users.equals("")){
                            users = article_detail.getComment_writers().get(i);
                        }
                        else if(check.get(i)){
                            users = users + "," + article_detail.getComment_writers().get(i);
                        }
                        else{

                        }
                    }
                    users = users + "," + nickName;
                    System.out.println("str = " + users);
                    try {
                        Edit_name = (EditText) findViewById(R.id.Edit_name);
                        String name = Edit_name.getText().toString();
                        String result  = new Invite_Task().execute(users, name).get();
                        Toast.makeText(chat_invite.this,"개설 되었습니",Toast.LENGTH_SHORT).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
                else{

                }
            }
        });
    }
}

class Invite_Task extends AsyncTask<String, Void, String> {
    public static String ip = "192.168.219.190:8080";
    String sendMessage, receiveMessage;
    String serverip = "http://"+ip+"/android/Create_chat.jsp";

    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL(serverip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            sendMessage = "str="+strings[0] + "&title=" + strings[1];
            osw.write(sendMessage);
            osw.flush();
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMessage = buffer.toString();
            }
            else {

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMessage;
    }
}