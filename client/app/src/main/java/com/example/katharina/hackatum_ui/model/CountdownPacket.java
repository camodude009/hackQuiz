package com.example.katharina.hackatum_ui.model;

public class CountdownPacket extends Packet{
    public static final String token = "CND";

    public long ms;

    public CountdownPacket(long ms) {
        setToken(this.token);
        this.ms = ms;
    }
}
