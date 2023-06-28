package com.example.termproject_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Chat_Adapter extends BaseAdapter {
    ArrayList<Chat> Chats = new ArrayList<Chat>();
    Context context;

    @Override
    public int getCount() {
        return Chats.size();
    }

    @Override
    public Object getItem(int position) {
        return Chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        Chat newChat = Chats.get(position);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_listview, viewGroup, false);
        }

        TextView chat_title = view.findViewById(R.id.chat_title);

        chat_title.setText(newChat.getTitle());

        return view;
    }

    public void Add_Chat(Chat newChat){
        Chats.add(newChat);
    }
}