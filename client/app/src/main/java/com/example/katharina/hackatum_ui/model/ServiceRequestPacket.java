package com.example.katharina.hackatum_ui.model;

public class ServiceRequestPacket extends Packet {

    public static final String token = "SRQ";

    public static int CALL_BARKEEPER = 1;
    public static int CALL_ORDER_DRINK = 2;

    public int service;

    public ServiceRequestPacket(int service) {
        setToken(this.token);
        this.service = service;
    }

    @Override
    public String toString() {
        return "ServiceRequestPacket{" +
                "service=" + service +
                '}';
    }
}
