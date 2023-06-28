package com.example.termproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class my_article extends AppCompatActivity {

    String ID, nickName;

    Button my_articleBtn;
    ListView my_article_view;

    Article_Adapter adapter;
    ArrayList<Article> my_articles;
    ArrayList<String> list_num;
    String[] jsonKey = {"date", "ID", "index", "comment_writer", "comment", "categori", "title", "content", "nickname"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_article);

        ID = getIntent().getStringExtra("userID");
        nickName = getIntent().getStringExtra("nickName");
        ReadDB();

        my_article_view.setAdapter(adapter);
        my_article_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                intent.putExtra("article_detail", my_articles.get(i));

                intent.putExtra("userID", ID);
                intent.putExtra("nickName", nickName);

                startActivity(intent);
                ReadDB();
            }
        });
    }

    public void ReadDB(){
        my_article_view = (ListView) findViewById(R.id.my_article_list);
        adapter = new Article_Adapter();

        my_articles = new ArrayList<Article>();
        list_num = new ArrayList<String>();

        try {
            String result  = new Article_Task().execute().get();
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject;

            for(int i = 0; i < jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject != null){
                    String date = jsonObject.getString(jsonKey[0]);
                    String userID = jsonObject.getString(jsonKey[1]);
                    String index = jsonObject.getString(jsonKey[2]);
                    String comment_writer = jsonObject.getString(jsonKey[3]);
                    String comment = jsonObject.getString(jsonKey[4]);
                    String categori = jsonObject.getString(jsonKey[5]);
                    String title = jsonObject.getString(jsonKey[6]);
                    String content = jsonObject.getString(jsonKey[7]);
                    String nick_name = jsonObject.getString(jsonKey[8]);


                    ArrayList<String> comments = new ArrayList<String>();
                    ArrayList<String> comment_writers = new ArrayList<String>();

                    if(comment.contains(",")){
                        String [] w = comment_writer.split(",");
                        String [] c = comment.split(",");

                        for(int j = 0; j < w.length; j++){
                            comment_writers.add(w[j]);
                            comments.add(c[j]);
                        }
                    }
                    else if(comment != null){
                        String w = comment_writer;
                        String c = comment;

                        comment_writers.add(w);
                        comments.add(c);
                    }
                    else{

                    }
                    if(nick_name.equals(nickName)){
                        Article newArticle = new Article(Integer.parseInt(index), categori, title, userID, date,
                                content, comments, comment_writers, nick_name);
                        my_articles.add(newArticle);
                        list_num.add(index);
                    }
                }
            }

            for(int i = my_articles.size()-1; i >= 0; i--){
                adapter.Add_Article(my_articles.get(i));
            }
            /*
            for(int i = 0; i < my_articles.size(); i--){
                adapter.Add_Article(my_articles.get(i));
            }*/

            /*
            for(int i = 0; i < articles.size(); i++){
                System.out.println(articles.get(i).getIndex());
                System.out.println(articles.get(i).getCategori());
                System.out.println(articles.get(i).getTitle());
                System.out.println(articles.get(i).getUserID());
                System.out.println(articles.get(i).getDate());
                System.out.println(articles.get(i).getContent());
                System.out.println(articles.get(i).getComment());
                System.out.println(articles.get(i).getComment_writer());

                System.out.println("\n\n" + articles.size());
            }*/
        }catch (Exception e) {}
    }
}