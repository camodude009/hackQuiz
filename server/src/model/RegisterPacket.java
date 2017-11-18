package model;

public class RegisterPacket extends Packet{

    public int table;
    public String name;

    public RegisterPacket(int table, String name){
        this.table = table;
        this.name = name;
    }
}
