package model;

public class AnswerPacket extends Packet {

    public static final String token = "ANS";

    public int table, answer, id;

    public AnswerPacket(int table, int answer, int id) {
        setToken(this.token);
        this.table = table;
        this.answer = answer;
        this.id = id;
    }

    public boolean equals(Object o) {
        return o instanceof AnswerPacket && ((AnswerPacket) o).id == this.id
                || o instanceof Integer && (int) o == this.id;
    }
}
