package me.mrnavastar.sqlib.impl.config;

import me.mrnavastar.sqlib.SQLib;
import org.quiltmc.loader.api.QuiltLoader;

import java.nio.file.Path;

public class Quilt extends SQLib {

    public static void load() {
        if (database == null) database = SQLibConfig.load(Path.of(QuiltLoader.getGameDir() + "/sqlib"), QuiltLoader.getConfigDir());
    }
}
