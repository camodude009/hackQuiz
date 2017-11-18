package com.example.katharina.hackatum_ui.model;

import java.sql.Time;

public class CountdownPacket extends Packet{
    public static final String token = "CND";

    public long ms;

    public CountdownPacket(long ms) {
        setToken(this.token);
        this.ms = ms;
    }

    public String getTime(){
        Time time = new Time(ms);
        return time.getMinutes()+":"+time.getSeconds();
    }
}
