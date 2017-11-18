import model.CountdownPacket;
import model.RegisterPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Registration extends Thread {

    public void run() {

        Log.log("starting registration thread...");

        try {

            Log.log("creating server socket...");
            ServerSocket serverSocket = new ServerSocket(Main.port);

            Log.log("initialised socket");
            Log.log("waiting for connections...");

            while (true) {

                Socket client = serverSocket.accept();
                Log.log("connection recieved");

                Table t = new Table(client);
                String message = t.read();

                if (message != null && message.startsWith(RegisterPacket.token)) {
                    RegisterPacket register = (RegisterPacket) Serializer.deserializePacket(message, RegisterPacket.class);
                    t.setTableNum(register.table);
                    t.setName(register.name);
                    synchronized (Main.tables) {
                        if (Main.tables.contains(t)) {
                            Main.tables.get(Main.tables.indexOf(t)).setSocket(client);
                            Log.log("re - registered" + t.toString());
                        } else {
                            Main.tables.add(t);
                            Log.log("registered" + t.toString());
                        }
                    }
                    t.start();

                    synchronized (t.packetQueue) {
                        if (Main.getCountdown() > 0) {
                            t.packetQueue.add(new CountdownPacket(Main.getCountdown()));
                        } else {
                            t.packetQueue.add(Main.getCurrentQuestion());
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.log(e.getMessage());
        }


    }
}
