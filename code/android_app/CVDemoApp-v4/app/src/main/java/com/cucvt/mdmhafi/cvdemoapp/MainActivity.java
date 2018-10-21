package com.cucvt.mdmhafi.cvdemoapp;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView txMessage , txSignal , txQueue;
    DSRCBoardcastReceiver receiver;
    Button btConnect ;
    Button btOBU ;
    RelativeLayout layout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        //txMessage = (TextView)findViewById(R.id.txMessage);
        layout = (RelativeLayout)findViewById(R.id.layout);


        txSignal = (TextView)findViewById(R.id.txMSignale);
        //txQueue = (TextView)findViewById(R.id.txQueue);

        btConnect = (Button) findViewById(R.id.btConnect);
        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Connecting to server...",Toast.LENGTH_SHORT).show();
                new ServerClient(mHandler);
            }
        });

        btOBU = (Button)findViewById(R.id.btOBU);
        btOBU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Connecting to Obu...",Toast.LENGTH_SHORT).show();
                receiver = new DSRCBoardcastReceiver(mHandler);
            }
        });
    }
    public final Handler mHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String message = msg.getData().getString("MESSAGE");
            if(message!=null){
                //Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();

                if(message.contains("RED") || message.contains("GREEN") || message.contains("YELLOW")){
                    //layout.setBackground(Color.R);
                    if(message.contains("RED")){
                        String time = message.split(",")[1];
                        txSignal.setText("RED\n" + time + " s");
                        txSignal.setBackgroundColor(Color.RED);
                    }
                    else if(message.contains("GREEN")){
                        String time = message.split(",")[1];
                        txSignal.setText("GREEN\n" + time + " s");
                        txSignal.setBackgroundColor(Color.GREEN);
                    }
                    else if(message.contains("YELLOW")){
                        String time = message.split(",")[1];
                        txSignal.setText("YELLOW\n" + time + " s");
                        txSignal.setBackgroundColor(Color.YELLOW);
                    }
                }
                if(message.contains("No Collision")){
                    //txMessage.setText("No Collision Ahead");
                   // layout.setBackgroundColor(Color.GREEN);
                }
                else if (message.contains("Collision")){
                   // txMessage.setText(message);
                    //layout.setBackgroundColor(Color.RED);
                }
                if(message.contains("No queue ahead")){
                    //txQueue.setText("No Queue Ahead");
                    //txQueue.setBackgroundColor(Color.GREEN);
                }
                else {
                    //txQueue.setText("Queue Ahead!");
                    //txQueue.setBackgroundColor(Color.RED);
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        if(receiver!=null){
            receiver.IsReceverRunning = false;
        }
        super.onDestroy();
    }
}