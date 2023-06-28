package com.example.termproject_1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Chat_Fragment extends Fragment {

    ListView chat_view;
    Chat_Adapter adapter;
    String userID, nickName;
    Button newBtn;

    ArrayList<Integer> list_num;
    ArrayList<Chat> chats;
    int index;

    String[] jsonKey = {"port", "title", "nickname"};
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chats = new ArrayList<Chat>();
        adapter = new Chat_Adapter();

        list_num = new ArrayList<Integer>();

        userID = this.getArguments().getString("userID");
        nickName = this.getArguments().getString("nickName");



        View v = inflater.inflate(R.layout.fragment_chat_, container, false);
        chat_view = (ListView) v.findViewById(R.id.chat_list);
        chat_view.setAdapter(adapter);

        //여기
        ReadDB();

        chat_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("채팅방")
                        .setMessage("채팅방을 나가시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){
                                try {
                                    String result  = new Chat_Out_Task().execute(Integer.toString(chats.get(i).getPort()),nickName).get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ReadDB();

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.DKGRAY);
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.DKGRAY);
                    }
                });

                dialog.show();
                return false;
            }
        });

        newBtn = (Button) v.findViewById(R.id.newBtn);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), new_chat.class);
                intent.putExtra("userID", userID);
                intent.putExtra("nickName", nickName);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        ReadDB();
        super.onResume();
    }

    public void ReadDB(){
        adapter = new Chat_Adapter();
        chats = new ArrayList<Chat>();
        try {
            String result  = new Chat_Task().execute(nickName).get();
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject;

            for(int i = 0; i < jsonArray.length(); i++){

                jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject != null){
                    String port = jsonObject.getString(jsonKey[0]);
                    String title = jsonObject.getString(jsonKey[1]);
                    String nickname = jsonObject.getString(jsonKey[2]);

                    String [] ids = nickname.split(",");

                    ArrayList<String> users = new ArrayList<String>();

                    for(int j = 0; j < ids.length; j++){
                        users.add(ids[j]);
                    }
                    System.out.println(ids.length);

                    Chat newChat = new Chat(title, Integer.parseInt(port), users);
                    chats.add(newChat);
                    list_num.add(i);
                }
            }
            for(int i = chats.size()-1; i >= 0; i--){
                adapter.Add_Chat(chats.get(i));
            }
        }catch (Exception e) {}


        chat_view.setAdapter(adapter);
        chat_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), chatting.class);
                intent.putExtra("userID", userID);
                intent.putExtra("port", chats.get(i).getPort());
                intent.putExtra("nickName", nickName);

                startActivity(intent);
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}



class Chat_Out_Task extends AsyncTask<String, Void, String> {
    public static String ip = "192.168.219.190:8080";
    String sendMsg, receiveMsg;
    String serverip = "http://"+ip+"/android/Chat_update.jsp";

    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL(serverip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            //sendMsg = "";
            sendMsg = "port=" + strings[0] +"&nickname="+strings[1];//+"&pass="+strings[1];
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