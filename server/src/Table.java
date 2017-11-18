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


    public Table(Socket socket) {
        this.socket = socket;

        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            Log.log(e.getMessage());
        }

        messageReciever = new Thread(() -> {
            while (true) {
                String s = read();
                if (s != null) tableHandler.handle(s);
                else {
                    Log.log(s);
                }
            }
        });

        messageSender = new Thread(() -> {
            while (true) {
                synchronized (packetQueue) {
                    while (packetQueue.isEmpty()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Log.log(e.getMessage());
                        }
                    }
                    Packet p = packetQueue.poll();
                    //TODO: send packet to client
                }
            }
        });

    }

    public void addTableHandler(TableHandler tableHandler) {
        this.tableHandler = tableHandler;
    }

    public boolean equals(Object o) {
        return o instanceof Table && ((Table) o).number == this.number;
    }


    public void write(String s) {
        out.write(s);
    }

    public String read() {
        try {
            return in.readLine();
        } catch (IOException e) {
            Log.log(e.getMessage());
        }
        return null;
    }

    public void setSocket(Socket client) {
        this.socket = client;
    }

}
