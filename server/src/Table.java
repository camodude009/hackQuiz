import model.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Queue;

public class Table {

    private Queue<Packet> packetQueue;


    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread messageReciever;
    private Thread messageSender;
    private TableHandler tableHandler;

    private int number;
    private boolean running;


    public Table(Socket client, TableHandler tableHandler) {
        this.tableHandler = tableHandler;
        setSocket(client);

        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            Log.log(e.getMessage());
        }

        messageReciever = new Thread(() -> {
            while (running) {
                try {
                    String s = in.readLine();
                    tableHandler.handle(s);
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
        messageReciever.start();
        messageSender.start();

    }

    public void terminate(){
        running = false;
        try {
            socket.close();
            messageReciever.join();
            messageSender.join();
        } catch (IOException e) {
            Log.log(e.getMessage());
        } catch (InterruptedException e) {
            Log.log(e.getMessage());
        }
    }

    public boolean equals(Object o) {
        return o instanceof Table && ((Table) o).number == this.number;
    }

    public void setSocket(Socket client) {
        this.socket = client;
        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            Log.log(e.getMessage());
        }
    }

    public int getNumber() {
        return number;
    }
}
