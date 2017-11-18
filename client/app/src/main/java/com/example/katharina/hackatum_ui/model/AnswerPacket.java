package com.example.katharina.hackatum_ui.model;

public class AnswerPacket extends Packet {

    public static final String token = "ANS";

    public int table;

    public AnswerPacket(int table) {
        setToken(this.token);
        this.table = table;
    }
}
