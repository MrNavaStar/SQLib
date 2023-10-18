package mrnavastar.sqlib;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.gson.Gson;
import lombok.Getter;
import mrnavastar.sqlib.config.ConfigUtil;
import mrnavastar.sqlib.config.SQLibConfig;
import mrnavastar.sqlib.database.Database;
import mrnavastar.sqlib.database.MySQLDatabase;
import mrnavastar.sqlib.database.SQLiteDatabase;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.HashMap;

public class SQLib implements ModInitializer {

    public static final String MOD_ID = "SQLib";
    public static final Gson GSON = new Gson();
    private static final HashMap<String, Database> databaseRegistry = new HashMap<>();
    @Getter
    private static Database database;
    private static SQLibConfig config = new SQLibConfig();

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> databaseRegistry.forEach((key, database) -> database.close()));

        CommentedFileConfig configFile = CommentedFileConfig.of(FabricLoader.getInstance().getConfigDir() + "/sqlib.toml");
        if (configFile.getFile().exists()) {
            configFile.load();
            try {
                config = new ObjectConverter().toObject(configFile, SQLibConfig::new);
            } catch (InvalidValueException e) {
                log(Level.ERROR, "Invalid config - Stopping");
                log(Level.ERROR, e.getMessage().replace(" for field java.lang.String mrnavastar.sqlib.config.SQLibConfig.type: it", "").replace("@com.electronwill.nightconfig.core.conversion.", ""));
                System.exit(1);
            }
        }
        else {
            config.sqlite.directory = System.getProperty("user.dir") + "/sqlib";
            new File(config.sqlite.directory).mkdirs();

            configFile.addAll(new ObjectConverter().toConfig(config, Config::inMemory));
            configFile.setComment("database.mysql", "testing testing");
            /*try {
                ConfigUtil.parseComments(config, configFile);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }*/
            configFile.save();
        }
        configFile.close();

        if (!config.database.enabled) {
            log(Level.WARN, "Internal database is disabled! This could potentially disable or break mods that use this feature!");
            return;
        }

        if (config.database.type.equalsIgnoreCase("SQLITE")) {
            if (!new File(config.sqlite.directory).exists()) {
                log(Level.ERROR, "Invalid config - Stopping");
                log(Level.ERROR, "[SQLite] Path: " + config.sqlite.directory + " was not found!");
                System.exit(1);
            }
            database = new SQLiteDatabase(MOD_ID, config.database.name, config.sqlite.directory);

        }
        else if (config.database.type.equalsIgnoreCase("MYSQL")) {
            database = new MySQLDatabase(MOD_ID, config.database.name, config.mysql.address, String.valueOf(config.mysql.port), config.mysql.username, config.mysql.password);
        }
    }

    public static void registerDatabase(String modId, String name,  Database database) {
        if (!databaseRegistry.containsKey(modId + name)) databaseRegistry.put(modId + name, database);
    }

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }

    public boolean isDatabaseEnabled() {
        return config.database.enabled;
    }
}