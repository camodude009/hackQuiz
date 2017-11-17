import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    final static int port = 4444;

    public static void main(String[] args){

        long init_time = System.currentTimeMillis();
        long registration_duration = 1000 * 10 * 60;

        while(System.currentTimeMillis() < init_time + registration_duration){

        }

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket clientSocket = serverSocket.accept();
                Client c = new Client(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
