package model;

public class SummaryPacket extends Packet {
    public static final String token = "SUM";

    public int total;
    public int correct;
    public int place;

    public SummaryPacket(int total, int correct, int place) {
        setToken(this.token);
        this.total = total;
        this.correct = correct;
        this.place = place;
    }

}
