package me.mrnavastar.sqlib;

import com.google.gson.Gson;
import lombok.Getter;
import me.mrnavastar.sqlib.config.Config;
import me.mrnavastar.sqlib.database.Database;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public sealed class SQLib permits Fabric, Velocity {

    public static final String MOD_ID = "SQLib";
    public static final Gson GSON = new Gson();

    private static final HashSet<Database> databases = new HashSet<>();
    @Getter
    protected static Database database;

    protected static void init(Path localDir, Path configDir) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> databases.forEach(Database::close)));
        localDir.toFile().mkdirs();
        database = Config.load(localDir, configDir);
    }

    public static void registerDatabase(Database database) {
        databases.add(database);
    }

    public static List<Database> getAllDatabases() {
        return databases.stream().toList();
    }

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }
}