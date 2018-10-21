package com.cucvt.mdmhafi.cvdemoapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by mdmhafi on 10/31/2017.
 */
public class ServerClient implements Runnable {
    public static boolean IsReceverRunning = false;
    Handler mHandler ;
    //public  static String SERVER_IP = "130.127.198.22";
    public  static String SERVER_IP = "18.234.19.54";
    int port = 9999;
    Socket socket;
    public  static final String TAG = "ServerClient";

    public ServerClient(Handler mHandler){
        this.mHandler = mHandler;
        IsReceverRunning = true;
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        try {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(SERVER_IP, port);
                new Thread(new ClientReadThread()).start();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }catch (Exception ex){
            Log.d(ServerClient.TAG, "Error in connecting:" + ex.toString());
        }
    }
    class ClientReadThread implements  Runnable{
        BufferedReader in;
        @Override
        public void run() {
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }catch (Exception ex){
                Log.d(ServerClient.TAG, "Error input stream::" + ex.toString());
                return;
            }
            try {


                String datain;
                while ((datain = in.readLine()) != null) {
                    String message = "No Queue Ahead";
                    if(datain.equals("0")){
                        Log.d(ServerClient.TAG, "Data In:" + "No Queue");
                        message = "No queue ahead";
                        //mHandler.sendMessage()
                    }else if(datain.equals("1")){
                        Log.d(ServerClient.TAG, "Data In:" +"Queue Ahdead");
                        message = "Queue ahead";
                    }
                    Message msgObj = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("MESSAGE", message );
                    msgObj.setData(b);
                    mHandler.sendMessage(msgObj);

                    //Log.d(ServerClient.TAG, "Data In:" + datain);
                }
                socket.close();
            }catch (Exception ex){
                Log.d(ServerClient.TAG, "Error in reading:" + ex.toString());
            }
        }
    }
}
