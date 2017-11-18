import java.util.ArrayList;
import java.util.List;

public class Main {

    public final static int port = 4444;
    public final static List<Table> tables = new ArrayList<>();

    public final static List<Question> questions = new ArrayList<>();

    public static void main(String[] args) {
        Log.log("starting server...");

        Registration registration = new Registration();
        registration.start();

        boolean running = true;
        long start_time = System.currentTimeMillis() + 60 * 5;

        try {
            Thread.sleep(start_time - System.currentTimeMillis());
        } catch (InterruptedException e) {
            Log.log(e.getMessage());
        }
    }
}
