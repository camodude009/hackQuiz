import model.AnswerPacket;
import model.Packet;
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
    private int score;
    private List<AnswerPacket> answers;

    public Table(Socket client) {
        packetQueue = new LinkedList<>();
        answers = new ArrayList<>();

        setSocket(client);

        messageReceiver = new Thread(() -> {
            while (running) {
                int numIllegitimateCalls = 0;
                String message = read();
                if (message != null) {
                    Log.log("received packet (" + table + "):" + message);
                    handle(message);
                } else {
                    numIllegitimateCalls++;
                    if (numIllegitimateCalls > 500) {
                        terminate();
                    }
                }
            }
        });

        messageSender = new Thread(() -> {
            while (running) {
                synchronized (packetQueue) {
                    while (running && packetQueue.isEmpty()) {
                        try {
                            packetQueue.wait();
                        } catch (InterruptedException e) {
                            Log.log(e.getMessage());
                        }
                    }
                    if (running) {
                        Packet p = packetQueue.poll();
                        String message = Serializer.serializeObject(p);
                        write(message);
                        Log.log("sent packet (" + table + "): " + message);
                    }
                }
            }
        });

    }

    public void handle(String message) {
        String token = message.substring(0, 3);

        switch (token) {
            case ServiceRequestPacket.token:
                ServiceRequestPacket service_request = (ServiceRequestPacket) Serializer.deserializePacket(message, ServiceRequestPacket.class);
                System.out.println("Request recieved from " + this.getNumber() + ": " + service_request);
                //TODO: further forwarding (LED, etc.)
                break;

            case AnswerPacket.token:
                AnswerPacket answer_request = (AnswerPacket) Serializer.deserializePacket(message, AnswerPacket.class);
                synchronized (answers) {
                    answers.remove(answer_request);
                    answers.add(answer_request);
                }
                calcScore();
                break;
        }

    }

    private void calcScore() {
        score = 0;
        synchronized (answers) {
            for (AnswerPacket a : answers) {
                if (Main.questions.get(Main.questions.indexOf(a.id)).correct == a.answer) score++;
            }
        }
    }

    public int getNumber() {
        return table;
    }

    public String read() {
        try {
            return in.readLine();
        } catch (IOException e) {
            this.terminate();
            Log.log(e.getMessage());
        }
        return null;
    }

    public void write(String s) {
        out.write(s + "\n");
        out.flush();
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
            this.terminate();
            Log.log(e.getMessage());
        }
    }

    public void terminate() {
        Log.log("table " + table + " being terminated...");
        running = false;
        synchronized (packetQueue) {
            packetQueue.removeAll(packetQueue);
        }
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
        Log.log("table " + table + " has terminated");
    }

    public List<AnswerPacket> getAnswers() {
        return answers;
    }

    public int getScore() {
        return score;
    }

    public void start() {
        running = true;
        messageReceiver.start();
        messageSender.start();
    }

    public boolean equals(Object o) {
        return o instanceof Table && ((Table) o).table == this.table || o instanceof Integer && (int) o == this.table;
    }

    public void setTableNum(int tableNum) {
        this.table = tableNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return running;
    }
}
