import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Log {

    //Test2

    public static void log(Object o) {
        System.out.println(getNiceDateTime(System.currentTimeMillis()) + "\t" + o.toString());
    }

    public static String getNiceDateTime(long your_milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        Date result_date = new Date(your_milliseconds);
        return sdf.format(result_date);
    }
}
