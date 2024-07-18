package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.impl.Config;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class Fabric extends SQLib {

    public static void init() {
        database = Config.load(Path.of(FabricLoader.getInstance().getGameDir() + "/sqlib"), FabricLoader.getInstance().getConfigDir());
    }
}