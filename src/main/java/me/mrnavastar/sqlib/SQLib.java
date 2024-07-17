package me.mrnavastar.sqlib;

import lombok.Getter;
import me.mrnavastar.sqlib.impl.Config;
import me.mrnavastar.sqlib.api.database.Database;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Path;
import java.util.List;

public class SQLib {

    @Getter
    protected static Database database;

    protected static void init(Path localDir, Path configDir) {
        database = Config.load(localDir, configDir);
    }

    public static List<Database> getDatabases() {
        return Database.getDatabases();
    }

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[SQLib] " + message);
    }
}