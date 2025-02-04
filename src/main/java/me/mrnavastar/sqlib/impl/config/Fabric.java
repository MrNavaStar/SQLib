package me.mrnavastar.sqlib.impl.config;

import me.mrnavastar.sqlib.SQLib;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class Fabric extends SQLib {

    public static void load() {
        if (database == null) database = SQLibConfig.load(Path.of(FabricLoader.getInstance().getGameDir() + "/sqlib"), FabricLoader.getInstance().getConfigDir());
    }
}