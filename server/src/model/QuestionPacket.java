package model;

public class QuestionPacket extends Packet {
    public static final String token = "QST";

    public int type;
    public int num;
    public int total;
    public String question;
    public String a, b, c ,d;
    public int correct;
    public long ms;

    public QuestionPacket(int type, int num, int total, String question, String a, String b, String c, String d, int correct, long ms) {
        setToken(this.token);
        this.type = type;
        this.num = num;
        this.total = total;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = correct;
        this.ms = ms;
    }
}
