import Customers.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Registration extends Thread {

    public void run(){
        Log.log("starting registration thread...");
        long init_time = System.currentTimeMillis();
        long registration_duration = 1000 * 10 * 60;

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

                new Thread(() -> {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String register = in.readLine();
                        String packetNr = register.substring(0, 3);
                        String content = register.substring(3);
                        Log.log("packetNr:" + packetNr);
                        Log.log("content:" + content);

                        if (register.startsWith("usr") && System.currentTimeMillis() < init_time + registration_duration) {
                            Person p = null;
                            synchronized (Main.persons) {
                                Main.persons.add(p);
                            }
                            Log.log("registered" + p.toString());
                        } else if (register.startsWith("tbl") && System.currentTimeMillis() >= init_time + registration_duration) {
                            Table t = null;
                            synchronized (Main.tables) {
                                Main.tables.remove(t);
                                Main.tables.add(t);
                            }
                            Log.log("registered" + t.toString());
                        }
                    } catch (IOException e) {
                        Log.log(e.getMessage());
                    }
                }).start();

            } catch (IOException e) {
                Log.log(e.getMessage());
            }
        }
    }
}
