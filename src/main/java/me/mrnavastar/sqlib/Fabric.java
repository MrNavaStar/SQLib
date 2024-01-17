package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.database.Database;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class Fabric extends SQLib implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> databases.forEach(Database::close));
        init(FabricLoader.getInstance().getGameDir(), FabricLoader.getInstance().getConfigDir());
    }
}