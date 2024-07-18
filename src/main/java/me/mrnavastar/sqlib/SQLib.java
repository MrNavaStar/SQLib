package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.impl.Config;
import me.mrnavastar.sqlib.api.database.Database;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Path;
import java.util.List;

public class SQLib {

    protected static Database database;

    public static Database getDatabase() {
        if (database != null) return database;

        try {
            Class.forName("net.fabricmc.loader.api.FabricLoader");
            Fabric.init();
            return database;
        } catch (ClassNotFoundException ignore) {}

        try {
            Class.forName("org.quiltmc.loader.api.QuiltLoader");
            Quilt.init();
            return database;
        } catch (ClassNotFoundException ignore) {}

        try {
            Class.forName("com.velocitypowered.api.plugin.Plugin");
            Velocity.init();
            return database;
        } catch (ClassNotFoundException ignore) {
            throw new RuntimeException("SQLib currently only support Fabric, Quilt, and Velocity!");
        }
    }

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