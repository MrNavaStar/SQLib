package me.mrnavastar.sqlib.impl.config;

import me.mrnavastar.sqlib.SQLib;

import java.nio.file.Path;

public class NonMinecraft extends SQLib {

    private static Path databaseDir;
    private static Path config;

    public static void init(Path defaultDatabaseDir, Path configDir) {
        databaseDir = defaultDatabaseDir;
        config = configDir;
    }

    public static boolean load() {
        if (databaseDir == null || config == null) return false;
        if (database == null) database = Config.load(databaseDir, config);
        return true;
    }
}
