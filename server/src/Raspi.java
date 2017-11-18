import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Raspi {

    public static void makeColorChange() {
        try {
            String serverAddress = "127.0.0.1";
            Socket s = new Socket(serverAddress, Main.port + 1);

            PrintWriter out = new PrintWriter(s.getOutputStream());
            out.write("change" + "\n");
            out.flush();
        } catch (IOException e) {
            Log.log(e.getMessage());
        }
    }
}
