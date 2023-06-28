package com.example.termproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class new_write extends AppCompatActivity {

    EditText write_title, write_content;
    Button write_btn;
    Spinner categori_spinner;
    String userID, nickName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_write);

        categori_spinner = (Spinner) findViewById(R.id.category);

        userID = getIntent().getStringExtra("userID");
        nickName = getIntent().getStringExtra("nickName");

        write_title = (EditText) findViewById(R.id.write_title);
        write_content = (EditText) findViewById(R.id.write_content);

        write_btn = (Button) findViewById(R.id.write_btn);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_title = write_title.getText().toString();
                String input_content = write_content.getText().toString();

                if((input_title.equals(""))||(input_content.equals(""))){
                    Toast.makeText(new_write.this,"제목 or 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        String categori_text = categori_spinner.getSelectedItem().toString();
                        String result  = new Write_Task().execute(input_title, input_content, userID, categori_text, nickName).get();
                        if(result.equals("true")) {
                            Toast.makeText(new_write.this,"작성되었습니다.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(new_write.this, home.class);
                            intent.putExtra("userID", userID);
                            intent.putExtra("nickName", nickName);

                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(new_write.this,"작성 실패",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e) {}
                }
            }
        });
    }
}