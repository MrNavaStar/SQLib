package me.mrnavastar.sqlib;

import org.bukkit.plugin.java.JavaPlugin;

public class Paper extends JavaPlugin {

    @Override
    public void onLoad() {
        SQLib.init(getDataFolder().toPath(), getDataFolder().toPath());
    }
}