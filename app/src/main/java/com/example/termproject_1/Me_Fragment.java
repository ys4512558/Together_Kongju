package com.example.termproject_1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Me_Fragment extends Fragment {

    String ID, nickName, name;

    Button my_articleBtn;
    TextView stdNum, NickName, Name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ID = this.getArguments().getString("userID");
        nickName = this.getArguments().getString("nickName");
        name = this.getArguments().getString("name");

        View v = inflater.inflate(R.layout.fragment_me_, container, false);

        stdNum = (TextView) v.findViewById(R.id.stdNum);
        NickName = (TextView) v.findViewById(R.id.nickName);
        Name = (TextView) v.findViewById(R.id.name);


        stdNum.setText("학번 : " + ID);
        NickName.setText("닉네임 : " + nickName);
        Name.setText("이름 : " + name);


        my_articleBtn = (Button) v.findViewById(R.id.my_articleBtn);
        my_articleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), my_article.class);
                intent.putExtra("userID", ID);
                intent.putExtra("nickName", nickName);

                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}