package com.example.termproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class chatting extends AppCompatActivity {
    boolean isConnect = false;
    EditText chat_Text;
    Button send_Btn;
    LinearLayout container;
    ScrollView scroll;
    boolean isRunning = false;
    Socket Chat_socket;
    int port;
    String userID, nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        userID = getIntent().getStringExtra("userID");
        port = getIntent().getIntExtra("port", 1);
        nickName = getIntent().getStringExtra("nickName");

        chat_Text = findViewById(R.id.chat_Text);
        send_Btn = findViewById(R.id.send_Btn);
        container = findViewById(R.id.container);
        scroll = findViewById(R.id.scroll);

        ConnectionThread thread = new ConnectionThread();
        thread.start();

        send_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chat_Text.getText().toString();
                SendToServerThread thread = new SendToServerThread(Chat_socket, msg);
                thread.start();
            }
        });
    }

    class ConnectionThread extends Thread {

        @Override
        public void run() {
            try {
                final Socket socket = new Socket("192.168.219.190",port);

                Chat_socket = socket;
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF(nickName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnect = true;
                        isRunning = true;
                        MessageThread thread = new MessageThread(socket);
                        thread.start();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MessageThread extends Thread {
        Socket socket;
        DataInputStream dis;

        public MessageThread(Socket socket) {
            try {
                this.socket = socket;
                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    final String msg = dis.readUTF();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            TextView tv = new TextView(chatting.this);
                            tv.setTextColor(Color.BLACK);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                            tv.setBackgroundResource(R.drawable.radiustv);
                            tv.setPadding(0,10,0,0);

                            tv.setText(msg);


                            container.addView(tv);
                            scroll.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SendToServerThread extends Thread {
        Socket socket;
        String msg;
        DataOutputStream dos;

        public SendToServerThread(Socket socket, String msg) {
            try {
                this.socket = socket;
                this.msg = msg;
                OutputStream os = socket.getOutputStream();
                dos = new DataOutputStream(os);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                dos.writeUTF(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chat_Text.setText("");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            isRunning = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
