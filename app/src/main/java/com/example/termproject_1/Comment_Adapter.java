package com.example.termproject_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Comment_Adapter extends BaseAdapter {

    ArrayList<String> cmt = new ArrayList<String>();
    ArrayList<String> cmt_writer = new ArrayList<String>();
    Context context;

    public void Add_comment(String comment, String comment_writer){
        cmt.add(comment);
        cmt_writer.add(comment_writer);
    }

    @Override
    public int getCount() {
        return cmt.size();
    }

    @Override
    public Object getItem(int i) {
        return cmt_writer.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        String comment = cmt.get(i);
        String comment_writer = cmt_writer.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.comment_listview, viewGroup, false);
        }

        TextView cmt = (TextView) view.findViewById(R.id.cmt);
        TextView cmt_writer = (TextView) view.findViewById(R.id.cmt_writer);


        cmt.setText(comment);
        cmt_writer.setText(comment_writer);

        return view;
    }
}
