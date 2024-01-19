package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.database.Database;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.nio.file.Path;

public class Fabric extends SQLib implements PreLaunchEntrypoint, DedicatedServerModInitializer, ClientModInitializer {

    @Override
    public void onPreLaunch() {
        init(Path.of(FabricLoader.getInstance().getGameDir() + "/sqlib"), FabricLoader.getInstance().getConfigDir());
    }

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> databases.forEach(Database::close));
    }

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> databases.forEach(Database::close));
    }
}