import model.QuestionPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        List<QuestionPacket> questions;

        QuestionRetriever r = new QuestionRetriever(100);
        questions = r.getQuestionPackets(100, 60 * 1000);
        for (QuestionPacket q : questions) {
            Log.log(q);
        }

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
            Socket client = serverSocket.accept();

            Table t = new Table(client);
            t.start();
            synchronized (t.packetQueue) {
                t.packetQueue.add(questions.get(0));
            }
            
            //String message = t.read();
            //Log.log(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
