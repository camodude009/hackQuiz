import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Registration extends Thread {

    public void run() {
        Log.log("starting registration thread...");

        Log.log("creating server socket...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Main.port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.log("initialised socket");
        Log.log("waiting for connections...");

        while (true) {
            try {

                Socket client = serverSocket.accept();
                Log.log("connection recieved");

                Table t = new Table(client);

                t.addTableHandler((String s)->{
                    String token = s.substring(0, 2);
                    String message = s.substring(3);

                    switch (token){
                        case "RGS":
                            //TODO: RegisterPacket r gson
                            t.setTableNum(r.tableNum);
                            t.setName(r.name);

                            break;
                        case "SRQ":
                            //TODO: forward request
                            break;
                        case "ANS":
                            //TODO: write answer to teams answers
                            break;
                    }
                });

                synchronized (Main.tables) {
                    Main.tables.remove(t);
                    Main.tables.add(t);
                }
                Log.log("registered" + t.toString());


            } catch (IOException e) {
                Log.log(e.getMessage());
            }
        }
    }
}
