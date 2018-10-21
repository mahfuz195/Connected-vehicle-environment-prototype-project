package com.cucvt.mdmhafi.cvdemoapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by mdmhafi on 10/20/2017.
 */
public class DSRCBoardcastReceiver implements Runnable{
    public static boolean IsReceverRunning = false;
    Handler mHandler ;
    int port = 9000;
    public DSRCBoardcastReceiver(Handler mHandler) {
        this.mHandler = mHandler;
        IsReceverRunning = true;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        DatagramSocket brsocket = null;
        int count  = 0 ;
        try{
            Log.d("DataIn", "Listening to port 1000");
            brsocket = new DatagramSocket(port, InetAddress.getByName("192.168.43.1"));
            brsocket.setBroadcast(true);
        }catch (Exception ex){}
        while (IsReceverRunning){
            try {
                //brsocket.setSoTimeout(10000);
                byte[] recvBuf = new byte[512];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                String message = "No data";
                try {
                    brsocket.receive(packet);
                    message = new String(packet.getData()).trim();
                    Log.d("DataIn", message);
                } catch (IOException ex) {
                    brsocket.close();
                    Log.d("D", String.format("No broadcast message: %s", ex.getMessage()));
                    return;
                }

                //Thread.sleep(1000);
                Message msgObj = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("MESSAGE", message);
                msgObj.setData(b);
                mHandler.sendMessage(msgObj);
                if(message.contains("COLLISION AHEAD")){
                    Message msgObj2 = mHandler.obtainMessage();
                    Bundle b2 = new Bundle();
                    b2.putString("MESSAGE", "Collision Ahead!");
                    msgObj2.setData(b2);
                    Thread.sleep(1000);
                    mHandler.sendMessage(msgObj2);
                }
                else if(message.contains("No Collision")){
                    Message msgObj2 = mHandler.obtainMessage();
                    Bundle b2 = new Bundle();
                    b2.putString("MESSAGE", "No Collision");
                    msgObj2.setData(b2);
                    Thread.sleep(500);
                    mHandler.sendMessage(msgObj2);
                }
                //Thread.sleep(5000);
                //count++;
            }catch (Exception ex) {
            }
        }
    }
}
