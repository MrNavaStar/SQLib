package me.mrnavastar.sqlib;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class Fabric extends SQLib {

    public static void init() {
        init(Path.of(FabricLoader.getInstance().getGameDir() + "/sqlib"), FabricLoader.getInstance().getConfigDir());
    }
}