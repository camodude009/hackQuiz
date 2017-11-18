import Customers.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public final static int port = 4444;
    public final static List<Table> tables = new ArrayList<Table>();
    public final static List<Person> persons = new ArrayList<Person>();

    public static void main(String[] args) {
        Log.log("starting server...");
        Registration registration = new Registration();
        registration.start();

        //TODO: matching

        //TODO: tell user the browser


    }
}
