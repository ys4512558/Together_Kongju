package com.example.termproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button SignUpBtn;
    Button LoginBtn;
    EditText id;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignUpBtn = (Button)findViewById(R.id.signUpBtn);
        LoginBtn = (Button)findViewById(R.id.loginBtn);
        id = (EditText) findViewById(R.id.id);
        pass = (EditText) findViewById(R.id.pass);

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), sign_up.class);
                startActivity(intent);
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_id = id.getText().toString();
                String input_pass = pass.getText().toString();
                try {
                    String result  = new Login_Task().execute(input_id, input_pass).get();
                    if(!result.equals("")) {
                        String temp[] = result.split(",");
                        Toast.makeText(MainActivity.this,"로그인",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, home.class);
                        intent.putExtra("userID", temp[0]);
                        intent.putExtra("nickName", temp[1]);
                        intent.putExtra("name", temp[2]);

                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"아이디 or 비밀번호가 다름",Toast.LENGTH_SHORT).show();
                        pass.setText("");
                    }
                }catch (Exception e) {}
            }
        });
    }
}