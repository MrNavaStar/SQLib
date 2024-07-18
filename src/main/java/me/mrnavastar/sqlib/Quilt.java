package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.impl.Config;
import org.quiltmc.loader.api.QuiltLoader;

import java.nio.file.Path;

public class Quilt extends SQLib {

    public static void init() {
        database = Config.load(Path.of(QuiltLoader.getGameDir() + "/sqlib"), QuiltLoader.getConfigDir());
    }
}
