package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.database.Database;
import org.bukkit.plugin.java.JavaPlugin;

public class Paper extends JavaPlugin {

    @Override
    public void onLoad() {
        SQLib.init(getDataFolder().toPath(), getDataFolder().toPath());
    }

    @Override
    public void onDisable() {
        SQLib.getAllDatabases().forEach(Database::close);
    }
}