package mrnavastar.sqlib;

import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class SQLib {

    public static final String MOD_ID = "SQLib";
    public static final JsonParser jsonParser = new JsonParser();

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }
}