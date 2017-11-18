import model.Packet;
import model.QuestionPacket;
import model.SummaryPacket;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public final static int port = 4444;
    public final static List<Table> tables = new ArrayList<>();


    public static final long registration_time = 1000 * 60;
    public static final long question_time = 1000 * 20;

    public static List<QuestionPacket> questions;
    private static QuestionPacket currentQuestion;

    private static long start_time;

    public static void main(String[] args) {

        /*
        QuizHttpServer httpServer = new QuizHttpServer(8000);

        httpServer.onMatching(matching -> {
            Log.log("matching recieved");
            Log.log(matching.toString());
            // play();
        });
        */

        play();

    }

    public static void play() {
        Log.log("loading questions...");


        QuestionRetriever retriever = new QuestionRetriever(100);
        questions = retriever.getQuestionPackets(3, question_time);

        Log.log("starting server...");

        Registration registration = new Registration();
        registration.start();


        boolean running = true;
        start_time = System.currentTimeMillis() + registration_time;

        sleep(start_time - System.currentTimeMillis() + 500);

        for (int i = 0; i < questions.size(); i++) {
            currentQuestion = questions.get(i);
            synchronized (tables) {
                for (Table t : tables) {
                    if (t.isRunning()) {
                        synchronized (t.packetQueue) {
                            t.packetQueue.add(currentQuestion);
                            t.packetQueue.notifyAll();
                        }
                    }
                }
            }
            sleep(question_time);
        }

        synchronized (tables) {
            tables.sort((a, b) -> {
                return Integer.compare(b.getScore(), a.getScore());
            });

            for (Table t : tables) {
                synchronized (t.packetQueue) {
                    t.packetQueue.add(new SummaryPacket(questions.size(), t.getScore(), tables.indexOf(t)));
                    t.packetQueue.notifyAll();
                }
            }
        }
    }

    public static void sleep(long millis) {
        Log.log("sleeping for " + millis / 1000 + "s");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.log(e.getMessage());
        }
    }

    public static Packet getCurrentQuestion() {
        return currentQuestion;
    }

    public static long getCountdown() {
        return start_time - System.currentTimeMillis();
    }
}
