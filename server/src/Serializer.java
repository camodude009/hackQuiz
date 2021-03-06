import com.google.gson.Gson;
import model.Packet;

public class Serializer {
    private static Gson gson = new Gson();

    public static String serializeObject(Packet p) {
        return p.getToken() + gson.toJson(p);
    }

    public static String getTokenFromPacket(String json) {
        return json.substring(0, 3);
    }

    public static Packet deserializePacket(String json, Class c) {
        //Json must be packet lol
        return (Packet) gson.fromJson(getJsonFromPacket(json), c);
    }

    public static String getJsonFromPacket(String json) {
        return json.substring(3);
    }

}
