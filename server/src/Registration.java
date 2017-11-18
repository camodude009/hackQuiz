import model.RegisterPacket;
import model.ServiceRequestPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Registration extends Thread {

    public void run() {
        Log.log("creating packet handler...");
        TableHandler tableHandler = (String message, Table t)-> {
            String token = message.substring(0, 3);



            switch (token) {
                case RegisterPacket.token:
                    RegisterPacket register = (RegisterPacket) Serializer.deserializePacket(message, RegisterPacket.class);
                    t.setTableNum(register.table);
                    t.setName(register.name);
                    break;

                case ServiceRequestPacket.token:
                    ServiceRequestPacket service_request = (ServiceRequestPacket) Serializer.deserializePacket(message, ServiceRequestPacket.class);
                    System.out.println("Request recieved from " + t.getNumber() + ": " + service_request);
                    //TODO: further forwarding (LED, etc.)
                    break;

                case "ANS":
                    //TODO: write answer to teams answers
                    break;
            }
        };

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

                Table t = new Table(client, tableHandler);

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
