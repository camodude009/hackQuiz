import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Raspi {

    public static final String ON = "on";
    public static final String OFF = "off";
    public static final String RAINBOW = "rainbow";
    public static final String RAINBOWCYCLE = "rainbowcycle";
    public static final String PREVIOUS = "prev";

    public static void animation(int r, int g, int b) {
        for (int i = 0; i < 8; i++) {
            makeColorChange(Raspi.OFF);
            Main.sleep(250);
            makeColorChange(Raspi.color(r, g, b));
            Main.sleep(250);
        }
        makeColorChange(Raspi.OFF);
    }

    public static void makeColorChange(String mode) {
        try {
            String serverAddress = "131.159.212.8";
            Socket s = new Socket(serverAddress, 4445);

            PrintWriter out = new PrintWriter(s.getOutputStream());
            out.write(mode);
            out.flush();
        } catch (IOException e) {
            Log.log(e.getMessage());
        }
    }

    public static String color(int r, int g, int b) {
        return "color:" + String.format("%03d", r & 0xff) + ","
                + String.format("%03d", g & 0xff) + ","
                + String.format("%03d", b & 0xff);
    }

}
