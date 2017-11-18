import model.AnswerPacket;
import model.Packet;
import model.RegisterPacket;
import model.ServiceRequestPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Table {

    public Queue<Packet> packetQueue;

    private boolean running;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread messageReceiver;
    private Thread messageSender;


    private int table;
    private String name;
    private List<AnswerPacket> answers;

    public void handle(String message) {
        String token = message.substring(0, 3);

        switch (token) {
            case ServiceRequestPacket.token:
                ServiceRequestPacket service_request = (ServiceRequestPacket) Serializer.deserializePacket(message, ServiceRequestPacket.class);
                System.out.println("Request recieved from " + this.getNumber() + ": " + service_request);
                //TODO: further forwarding (LED, etc.)
                break;

            case AnswerPacket.token:
                //AnswerPacket answer_request = (ServiceRequestPacket) Serializer.deserializePacket(message, ServiceRequestPacket.class);
                //TODO: set answer
                break;
        }
    }

    public Table(Socket client) {
        packetQueue = new LinkedList<>();
        answers = new ArrayList<>();

        setSocket(client);

        messageReceiver = new Thread(() -> {
            while (running) {
                String s = read();
                handle(s);
            }
        });

        messageSender = new Thread(() -> {
            while (running) {
                synchronized (packetQueue) {
                    while (running && packetQueue.isEmpty()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Log.log(e.getMessage());
                        }
                    }
                    if (running) {
                        Packet p = packetQueue.poll();
                        String message = Serializer.serializeObject(p);
                        write(message);
                    }
                }
            }
        });

    }

    public String read() {
        try {
            return in.readLine();
        } catch (IOException e) {
            Log.log(e.getMessage());
        }
        return "";
    }

    public void write(String s) {
        out.write(s);
    }

    public void start() {
        running = true;
        messageReceiver.start();
        messageSender.start();
    }

    public void terminate() {
        running = false;
        try {
            socket.close();
            synchronized (packetQueue) {
                packetQueue.notifyAll();
            }
            messageReceiver.join();
            messageSender.join();
        } catch (IOException e) {
            Log.log(e.getMessage());
        } catch (InterruptedException e) {
            Log.log(e.getMessage());
        }
    }

    public boolean equals(Object o) {
        return o instanceof Table && ((Table) o).table == this.table || o instanceof Integer && (int) o == this.table;
    }

    public void setSocket(Socket client) {
        if (this.socket != null) {
            terminate();
        }
        this.socket = client;
        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            Log.log(e.getMessage());
        }
    }

    public int getNumber() {
        return table;
    }

    public void setTableNum(int tableNum) {
        this.table = table;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setAnswer(AnswerPacket a) {
        answers.remove(a);
        answers.add(a);
    }
}
