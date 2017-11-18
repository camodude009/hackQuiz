package model.old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(Socket clientSocket) {
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void write(String s){
        out.write(s);
    }

    public void read(String s){
        try {
            in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
