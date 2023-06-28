package com.example.termproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sign_up extends AppCompatActivity {

    Button checkID, signUpBtn;
    EditText id, pass, name, phone, email, nickname;
    boolean checkNum = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        checkID = (Button) findViewById(R.id.checkID);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);

        id = (EditText) findViewById(R.id.id);
        pass = (EditText) findViewById(R.id.pass);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        nickname = (EditText) findViewById(R.id.nickname);



        checkID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_id = id.getText().toString();
                try {
                    String result  = new SignUp_Task().execute("check",input_id).get();
                    if(result.equals("true")) {
                        int stdNum = Integer.parseInt(input_id);
                        if((stdNum >= 200000000)&& stdNum <= 299999999){
                            Toast.makeText(sign_up.this,"사용가능한 학번입니다.",Toast.LENGTH_SHORT).show();
                            id.setEnabled(false);
                            checkNum = true;
                        }
                        else{
                            Toast.makeText(sign_up.this,"학번을 확인해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(sign_up.this,"이미 존재하는 학번입니다.",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {}
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_id = id.getText().toString();
                String input_pass = pass.getText().toString();
                String input_name = name.getText().toString();
                String input_phone = phone.getText().toString();
                String input_email = email.getText().toString();
                String input_nickname = nickname.getText().toString();


                if((input_id.equals("")) || (input_pass.equals("")) || (input_name.equals(""))
                        || (input_phone.equals("")) || (input_email.equals("")) || (input_nickname.equals(""))){
                    Toast.makeText(sign_up.this,"빈 항목을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(!checkNum){
                    Toast.makeText(sign_up.this,"학번확인을 해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        String result  = new SignUp_Task().
                                execute("signUp", input_id, input_pass, input_name, input_phone, input_email, input_nickname).get();
                        if(result.equals("true")) {
                            Toast.makeText(sign_up.this,"회원가입 완료!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(sign_up.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(sign_up.this,"회원가입 실패",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e) {}
                }
            }
        });
    }
}