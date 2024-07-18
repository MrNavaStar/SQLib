package me.mrnavastar.sqlib;

import java.nio.file.Path;

public class Velocity extends SQLib {

    public static void init() {
        Path dir = Path.of("plugins/sqlib");
        init(dir,dir);
    }
}
