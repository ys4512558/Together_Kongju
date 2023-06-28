package com.example.termproject_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Article_Adapter extends BaseAdapter {
    ArrayList<Article> articles = new ArrayList<Article>();
    Context context;

    public void Add_Article(Article newArticle){
        articles.add(newArticle);
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        Article newArticle = articles.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.article_listview, viewGroup, false);
        }

        TextView article_title = view.findViewById(R.id.article_title);
        TextView article_name = view.findViewById(R.id.article_name);
        TextView article_date = view.findViewById(R.id.article_date);

        article_title.setText(newArticle.getTitle());
        article_name.setText(newArticle.getNickName());
        article_date.setText(newArticle.getDate());

        return view;
    }

}
