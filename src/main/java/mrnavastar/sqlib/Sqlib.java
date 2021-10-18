package mrnavastar.sqlib;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Sqlib implements ModInitializer {

    private static final String MODID = "SQLib";

    @Override
    public void onInitialize() {

    }

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MODID + "] " + message);
    }
}