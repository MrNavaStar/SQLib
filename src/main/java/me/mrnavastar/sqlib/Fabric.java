package me.mrnavastar.sqlib;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.nio.file.Path;

public non-sealed class Fabric extends SQLib implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        init(Path.of(FabricLoader.getInstance().getGameDir() + "/sqlib"), FabricLoader.getInstance().getConfigDir());
    }
}