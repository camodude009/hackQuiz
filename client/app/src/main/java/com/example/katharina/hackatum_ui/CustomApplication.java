package com.example.katharina.hackatum_ui;

import com.example.katharina.hackatum_ui.model.Packet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bloeh on 18.11.2017.
 */

public class CustomApplication extends android.app.Application {

    private int tableNum;
    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    private Queue<Packet> messageQeuue = new ConcurrentLinkedQueue<>();
    public Queue<Packet> getMessageQeuue() {
        return messageQeuue;
    }

    public void setMessageQeuue(Queue<Packet> messageQeuue) {
        this.messageQeuue = messageQeuue;
    }



    @Override
    public void onCreate() {
        super.onCreate();
    }

}
