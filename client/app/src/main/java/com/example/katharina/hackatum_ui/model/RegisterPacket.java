package com.example.katharina.hackatum_ui.model;

public class RegisterPacket extends Packet{
    public static final String token = "RGS";

    public int table;
    public String name;

    public RegisterPacket(int table, String name){
        setToken(this.token);
        this.table = table;
        this.name = name;
    }
}
