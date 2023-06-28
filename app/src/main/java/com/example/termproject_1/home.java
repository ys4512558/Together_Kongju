package com.example.termproject_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NavigationRes;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class home extends AppCompatActivity {

    Home_Fragment home_fragment;
    Chat_Fragment chat_fragment;
    Me_Fragment me_fragment;
    String userID, nickName, name;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        userID = getIntent().getStringExtra("userID");
        nickName = getIntent().getStringExtra("nickName");
        name = getIntent().getStringExtra("name");

        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putString("nickName", nickName);


        home_fragment = new Home_Fragment();
        chat_fragment = new Chat_Fragment();
        me_fragment = new Me_Fragment();

        home_fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, home_fragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_menu);
        navigationBarView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.tab_home:
                                home_fragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().
                                        replace(R.id.container, home_fragment).
                                        commitAllowingStateLoss();
                                return true;

                            case R.id.tab_chat:
                                chat_fragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().
                                        replace(R.id.container, chat_fragment).
                                        commitAllowingStateLoss();
                                return true;

                            case R.id.tab_me:
                                bundle.putString("name", name);
                                me_fragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().
                                        replace(R.id.container, me_fragment).
                                        commitAllowingStateLoss();
                                return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}