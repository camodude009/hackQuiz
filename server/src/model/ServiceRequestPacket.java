package model;

public class ServiceRequestPacket extends Packet {

    public static final String token = "SRQ";

    public static int CALL_BARKEEPER = 1;
    public static int CALL_ORDER_DRINK = 2;

    public static int CHOOSE_COLOR = 3;
    public static int RATE_MUSIC = 4;


    public int service;
    public String data;

    public ServiceRequestPacket(int service) {
        this(service, null);
    }

    public ServiceRequestPacket(int service, String data) {
        setToken(this.token);
        this.service = service;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ServiceRequestPacket{" +
                "service=" + service +
                ", data='" + data + '\'' +
                '}';
    }
}
