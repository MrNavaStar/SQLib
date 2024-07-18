package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.impl.Config;

import java.nio.file.Path;

public class Velocity extends SQLib {

    public static void init() {
        Path dir = Path.of("plugins/sqlib");
        database = Config.load(dir,dir);
    }
}
