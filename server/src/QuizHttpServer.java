import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class QuizHttpServer {

    private static Gson gson = new Gson();
    private HttpServer server;
    private MatchingListener matchingListener;

    public QuizHttpServer(int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.createContext("/matching", new MatchingHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public void onMatching(MatchingListener listener) {
        matchingListener = listener;
    }

    public interface MatchingListener {
        void onMatchingCompleted(MatchingPacket matching);
    }

    public class MatchingHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            BufferedReader in = new BufferedReader(new InputStreamReader(t.getRequestBody()));
            String inputLine;
            String json = "";
            while ((inputLine = in.readLine()) != null)
                json += inputLine;
            in.close();

            t.sendResponseHeaders(200, 0);
            t.close();

            MatchingPacket matching = (MatchingPacket) gson.fromJson(json, MatchingPacket.class);

            if (matchingListener != null) {
                matchingListener.onMatchingCompleted(matching);
            }
        }
    }

    public class MatchingPacket {

        public MatchedTablesPacket[] matching;

        public MatchingPacket(MatchedTablesPacket[] mat) {
            matching = mat;
        }

        public class MatchedTablesPacket {

            public int table;
            public String[] users;

            public MatchedTablesPacket(int tab, String[] usn) {
                table = tab;
                users = usn;
            }


            @Override
            public String toString() {
                return "MatchedTablesPacket{" +
                        "tableId=" + table +
                        ", usernames=" + Arrays.toString(users) +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "MatchingPacket{" +
                    "matchedTables=" + Arrays.toString(matching) +
                    '}';
        }
    }
}
