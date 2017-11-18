package model;

public class ServiceRequestPacket extends Packet {

    public static final String token = "SRQ";

    public static int CHANGE_COLOR = 0;
    public static int CALL_BARKEEPER = 1;
    public static int CALL_ORDER_DRINK = 2;
    public static int MUSIC = 3;
    public static int DRINK_RECOMMENDATION = 4;

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
