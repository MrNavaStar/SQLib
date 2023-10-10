package mrnavastar.sqlib;

import com.google.gson.Gson;
import mrnavastar.sqlib.database.Database;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;

public class SQLib implements ModInitializer {

    public static final String MOD_ID = "SQLib";
    public static final Gson GSON = new Gson();
    private static final ArrayList<Database> databaseRegistry = new ArrayList<>();

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> databaseRegistry.forEach(Database::close));
    }

    public static void registerDatabase(Database database) {
        if (!databaseRegistry.contains(database)) databaseRegistry.add(database);
    }

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }
}