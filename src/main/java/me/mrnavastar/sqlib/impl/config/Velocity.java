package me.mrnavastar.sqlib.impl.config;

import me.mrnavastar.sqlib.SQLib;

import java.nio.file.Path;

public class Velocity extends SQLib {

    public static void load() {
        if (database != null) return;
        Path dir = Path.of("plugins/sqlib");
        database = SQLibConfig.load(dir,dir);
    }
}
