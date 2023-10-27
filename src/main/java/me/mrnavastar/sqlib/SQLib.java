package me.mrnavastar.sqlib;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.google.gson.Gson;
import lombok.Getter;
import me.mrnavastar.sqlib.config.SQLibConfig;
import me.mrnavastar.sqlib.database.Database;
import me.mrnavastar.sqlib.database.MySQLDatabase;
import me.mrnavastar.sqlib.database.SQLiteDatabase;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;

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
        new File(FabricLoader.getInstance().getGameDir() + "/sqlib").mkdirs();

        try {
            File configFile = new File(FabricLoader.getInstance().getConfigDir() + "/sqlib.toml");
            if (!configFile.exists()) {
                Files.copy(Objects.requireNonNull(SQLib.class.getResourceAsStream("/sqlib.toml")), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            TomlMapper mapper = new TomlMapper();
            config = mapper.readValue(configFile, SQLibConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (config.database.type.equalsIgnoreCase("SQLITE")) {
            if (!config.validateSQLite()) {
                log(Level.ERROR, "Invalid config - Stopping");
                System.exit(1);
            }

            if (!new File(config.sqlite.directory).exists()) {
                log(Level.ERROR, "Invalid config - Stopping");
                log(Level.ERROR, "[SQLite] Path: " + config.sqlite.directory + " was not found!");
                System.exit(1);
            }
            database = new SQLiteDatabase(MOD_ID, config.database.name, config.sqlite.directory);
        }
        else if (config.database.type.equalsIgnoreCase("MYSQL")) {
            if (!config.validateMySQL()) {
                log(Level.ERROR, "Invalid config - Stopping");
                System.exit(1);
            }

            database = new MySQLDatabase(MOD_ID, config.database.name, config.mysql.address, String.valueOf(config.mysql.port), config.mysql.username, config.mysql.password);
        }
    }

    public static void registerDatabase(String modId, String name, Database database) {
        if (!databaseRegistry.containsKey(modId + name)) databaseRegistry.put(modId + name, database);
    }

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }
}