import model.Packet;
import model.QuestionPacket;
import model.SummaryPacket;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public final static int port = 4444;
    public final static List<Table> tables = new ArrayList<>();

    public final static List<QuestionPacket> questions = new ArrayList<>();

    public static final long registration_time = 1000 * 60 * 5;
    public static final long question_time = 1000 * 60;

    private static QuestionPacket currentQuestion;

    public static void main(String[] args) {
        Log.log("starting server...");

        Registration registration = new Registration();
        registration.start();


        boolean running = true;
        long start_time = System.currentTimeMillis() + registration_time;

        sleep(start_time - System.currentTimeMillis());

        for (int i = 0; i < questions.size(); i++) {
            currentQuestion = questions.get(i);
            synchronized (tables) {
                for (Table t : tables) {
                    synchronized (t.packetQueue) {
                        t.packetQueue.add(currentQuestion);
                        t.packetQueue.notifyAll();
                    }
                }
            }
            sleep(question_time);
        }

        for (Table t : tables) {
            synchronized (t.packetQueue) {
                //TODO: calculate correct and palce
                int correct = 0;
                int place = 0;
                t.packetQueue.add(new SummaryPacket(questions.size(), correct, place));
                t.packetQueue.notifyAll();
            }
        }


    }

    public static void sleep(long millis) {
        Log.log("sleeping for" + millis + "ms");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.log(e.getMessage());
        }
    }

    public static Packet getCurrentQuestion() {
        return currentQuestion;
    }
}
