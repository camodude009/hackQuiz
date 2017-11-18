import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class QuizHttpServer {

  private HttpServer server;
  private MatchingListener matchingListener;

  public QuizHttpServer(int port) {
    server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/matching", new MatchingHandler());

    server.setExecutor(null); // creates a default executor
    server.start();
  }

  public class MatchingHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {

      BufferedReader in = new BufferedReader(new InputStreamReader(t.getRequestBody()));
      String inputLine; String json = "";
      while ((inputLine = in.readLine()) != null)
          json += inputLine;
      in.close();
      t.sendResponseHeaders(200, 0);

      MatchingPacket matching =  (MatchingPacket) gson.fromJson(json, MatchingPacket.class);

      if (matchingListener != null) {
        matchingListener.onMatchingCompleted(matching);
      }
    }
  }

  public void onMatching(MatchingListener listener) {
    matchingListener = listener;
  }

  public interface MatchingListener {
    onMatchingCompleted(MatchingPacket matching);
  }

  public class MatchingPacket {

    public MatchedTables[] matchedTables;

    public MatchingPacket(MatchedTablesPacket[] mat) {
      matchedTables = mat;
    }

    public class MatchedTablesPacket {

      public int tableId;
      public String[] usernames;

      public MatchedTablesPacket(int tab, String[] usn) {
        tableId = tab;
        usernames = usn;
      }

    }

  }
}
