package mrnavastar.sqlib;

import com.google.gson.Gson;
import lombok.Getter;
import mrnavastar.sqlib.database.Database;
import mrnavastar.sqlib.database.SQLiteDatabase;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;

public class SQLib implements ModInitializer {

    public static final String MOD_ID = "SQLib";
    public static final Gson GSON = new Gson();
    private static final HashMap<String, Database> databaseRegistry = new HashMap<>();
    @Getter
    private static Database database;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> databaseRegistry.forEach((key, database) -> database.close()));
    }

    public static void registerDatabase(String modId, String name,  Database database) {
        if (!databaseRegistry.containsKey(modId + name)) databaseRegistry.put(modId + name, database);
        SQLib.database = new SQLiteDatabase(MOD_ID, "sqlib-default", "");
    }

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }
}