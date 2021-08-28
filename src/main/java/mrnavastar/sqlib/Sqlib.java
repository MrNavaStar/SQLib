package mrnavastar.sqlib;

import mrnavastar.sqlib.util.Database;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;


public class Sqlib implements ModInitializer {
    @Override
    public void onInitialize() {
        Database.init();
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> Database.saveAll());
    }
}