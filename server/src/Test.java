import java.awt.*;

public class Test {

    public static void main(String[] args) {

       /*
        List<QuestionPacket> questions;


        QuestionRetriever r = new QuestionRetriever(100);
        questions = r.getQuestionPackets(100, 60 * 1000);
        Log.log("loaded " + questions.size() + " questions");

        ServerSocket serverSocket = null;

        try {
            Log.log("creating server socket...");
            serverSocket = new ServerSocket(4444);

            while (true) {
                Socket client = serverSocket.accept();
                Log.log("client accepted");

                Table t = new Table(client);
                Log.log("starting client...");
                t.start();
                synchronized (t.packetQueue) {
                    t.packetQueue.add(questions.get(0));
                }
            }
            //String message = t.read();
            //Log.log(message);
        } catch (IOException e) {
            Log.log(e.getMessage());
        }

        */


        //Raspi.makeColorChange(Raspi.RAINBOWCYCLE);
        Raspi.makeColorChange(Raspi.RAINBOWCYCLE);
        //Raspi.animation(255,255,0);

    }
}
