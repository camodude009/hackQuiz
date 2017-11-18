import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Table extends Thread{

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean running;
    private TableHandler tableHandler;
    private int number;

    public Table(Socket socket, TableHandler tableHandler, int number) {
        this.socket = socket;
        this.tableHandler = tableHandler;
        this.number = number;
        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            Log.log(e.getMessage());
        }
        running = true;
        start();
    }

    public boolean equals(Object o){
         return o instanceof Table && ((Table)o).number == this.number;
    }

    public void run() {
        while (running) {
            String s = read();
            if(s != null)tableHandler.handle(s);
            else{Log.log(s);}
        }
    }

    public void terminate() {
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            Log.log(e.getMessage());
        }
        try {
            this.join();
        } catch (InterruptedException e) {
            Log.log(e.getMessage());
        }
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

}
