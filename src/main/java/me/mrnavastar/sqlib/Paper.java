package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.database.Database;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class Paper extends JavaPlugin {

    @Override
    public void onLoad() {
        SQLib.init(getDataFolder().toPath(), Path.of("./config"));
    }

    @Override
    public void onDisable() {
        SQLib.getAllDatabases().forEach(Database::close);
    }
}