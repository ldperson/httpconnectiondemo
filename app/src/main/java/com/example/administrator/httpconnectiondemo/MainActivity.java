package com.example.administrator.httpconnectiondemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button send;
    private TextView responseText;
    private static final int SHOW_RESPONSE=0;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==SHOW_RESPONSE){
                String response= (String) msg.obj;
                responseText.setText(response);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResquestWithUrlConnection();
            }
        });
    }
    private void sendResquestWithUrlConnection() {
        //开启线程发送请求
        new Thread(new Runnable() {

            private HttpURLConnection conn;

            @Override
            public void run() {
                try {
                    URL url=new URL("http://www.baidu.com");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    InputStream inputStream = conn.getInputStream();
                    //对获取到的输入流进行读取
                    InputStreamReader in=new InputStreamReader(inputStream);
                    BufferedReader reader=new BufferedReader(in);
                    StringBuilder builder=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        builder.append(line);
                    }
                    Message message=new Message();
                    message.what=SHOW_RESPONSE;
                    message.obj=builder.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (conn!=null){
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
}
