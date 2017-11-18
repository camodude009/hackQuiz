import model.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Table {

    public Queue<Packet> packetQueue;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread messageReceiver;
    private Thread messageSender;
    private TableHandler tableHandler;

    private int table;
    private String name;
    private boolean running;


    public Table(Socket client, TableHandler tableHandler) {
        packetQueue = new ConcurrentLinkedQueue<>();
        this.tableHandler = tableHandler;
        setSocket(client);

        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            Log.log(e.getMessage());
        }

        messageReceiver = new Thread(() -> {
            while (running) {
                try {
                    String s = in.readLine();
                    tableHandler.handle(s, this);
                } catch (IOException e) {
                    Log.log(e.getMessage());
                }
            }
        });

        messageSender = new Thread(() -> {
            while (running) {
                synchronized (packetQueue) {
                    while (packetQueue.isEmpty()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Log.log(e.getMessage());
                        }
                    }
                    Packet p = packetQueue.poll();
                    String message = Serializer.serializeObject(p);
                    out.write(message);
                }
            }
        });

        running = true;
        messageReceiver.start();
        messageSender.start();

    }

    public void terminate() {
        running = false;
        try {
            socket.close();
            messageReceiver.join();
            messageSender.join();
        } catch (IOException e) {
            Log.log(e.getMessage());
        } catch (InterruptedException e) {
            Log.log(e.getMessage());
        }
    }

    public boolean equals(Object o) {
        return o instanceof Table && ((Table) o).table == this.table;
    }

    public void setSocket(Socket client) {
        if (this.socket != null) try {
            socket.close();
        } catch (IOException e) {
            Log.log(e.getMessage());
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
}
