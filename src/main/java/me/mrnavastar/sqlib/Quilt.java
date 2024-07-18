package me.mrnavastar.sqlib;

import org.quiltmc.loader.api.QuiltLoader;

import java.nio.file.Path;

public class Quilt extends SQLib {

    public static void init() {
        init(Path.of(QuiltLoader.getGameDir() + "/sqlib"), QuiltLoader.getConfigDir());
    }
}
