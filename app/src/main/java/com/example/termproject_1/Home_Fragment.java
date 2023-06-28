package com.example.termproject_1;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home_Fragment extends Fragment {

    Button writeBtn, searchBtn;
    EditText search_text;
    Spinner categori_spinner;
    ListView article_view;
    Article_Adapter adapter;

    ArrayList<String> list_num;
    ArrayList<Article> articles;
    ArrayList<Article> search_articles;

    String userID, nickName;
    String categori_text;

    int check;
    String[] jsonKey = {"date", "ID", "index", "comment_writer", "comment", "categori", "title", "content", "nickname"};

    View v;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        check = -1;
        userID = getArguments().getString("userID");
        nickName = getArguments().getString("nickName");

        v = inflater.inflate(R.layout.fragment_home_, container, false);
        ReadDB(v);

        article_view.setAdapter(adapter);
        article_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), DetailActivity.class);

                if(check == -1){
                    intent.putExtra("article_detail", articles.get(articles.size() - i - 1));
                }
                else{
                    intent.putExtra("article_detail", search_articles.get(i));
                }
                intent.putExtra("userID", userID);
                intent.putExtra("nickName", nickName);

                startActivity(intent);

                ReadDB(v);
            }
        });

        writeBtn = (Button) v.findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), new_write.class);

                intent.putExtra("userID", userID);
                intent.putExtra("nickName", nickName);
                startActivity(intent);
            }
        });

        categori_spinner = (Spinner) v.findViewById(R.id.category);
        categori_text = categori_spinner.getSelectedItem().toString();
        search_text = (EditText) v.findViewById(R.id.search_text);
        search_articles = new ArrayList<Article>();
        searchBtn = (Button) v.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 1;
                adapter = new Article_Adapter();
                search_articles = new ArrayList<Article>();
                String search = search_text.getText().toString();
                for(int i = articles.size()-1; i >= 0; i--){
                    if(articles.get(i).getTitle().contains(search) && categori_text.equals("ALL")){
                        search_articles.add(articles.get(i));
                    }
                    else if(articles.get(i).getTitle().contains(search) && articles.get(i).getCategori().equals(categori_text)){
                        search_articles.add(articles.get(i));
                    }
                    else{

                    }
                }
                for(int i = 0; i < search_articles.size(); i++){
                    adapter.Add_Article(search_articles.get(i));
                }
                article_view.setAdapter(adapter);
                search_text.setText("");
            }
        });

        categori_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categori_text = categori_spinner.getSelectedItem().toString();

                adapter = new Article_Adapter();
                search_articles = new ArrayList<Article>();
                for(int j = articles.size()-1; j >= 0; j--){
                    if(categori_text.equals("ALL")){
                        search_articles.add(articles.get(j));
                    }
                    else if(articles.get(j).getCategori().equals(categori_text)){
                        search_articles.add(articles.get(j));
                    }
                }
                for(int j = 0;  j < search_articles.size(); j++){
                    adapter.Add_Article(search_articles.get(j));
                }
                article_view.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        ReadDB(v);
        super.onResume();
    }

    public void ReadDB(View v){
        article_view = (ListView) v.findViewById(R.id.article_list);
        adapter = new Article_Adapter();

        articles = new ArrayList<Article>();
        list_num = new ArrayList<String>();

        try {
            String result  = new Article_Task().execute().get();
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject;

            for(int i = 0; i < jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject != null){
                    String date = jsonObject.getString(jsonKey[0]);
                    String ID = jsonObject.getString(jsonKey[1]);
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

                    Article newArticle = new Article(Integer.parseInt(index), categori, title, ID, date,
                            content, comments, comment_writers, nick_name);
                    articles.add(newArticle);
                    list_num.add(index);
                }
            }
            for(int i = articles.size()-1; i >= 0; i--){
                adapter.Add_Article(articles.get(i));
            }
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
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}