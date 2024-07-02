package me.mrnavastar.sqlib;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.google.gson.Gson;
import lombok.Getter;
import me.mrnavastar.sqlib.config.SQLibConfig;
import me.mrnavastar.sqlib.database.Database;
import me.mrnavastar.sqlib.database.MySQLDatabase;
import me.mrnavastar.sqlib.database.SQLiteDatabase;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class SQLib {

    public static final String MOD_ID = "SQLib";
    public static final Gson GSON = new Gson();

    private static final HashSet<Database> databases = new HashSet<>();
    @Getter
    protected static Database database;
    protected static SQLibConfig config = new SQLibConfig();

    public static void init(Path sqliteDataDir, Path configDir) {
        sqliteDataDir.toFile().mkdirs();

        try {
            File configFile = new File(configDir + "/sqlib.toml");
            if (!configFile.exists()) {
                String data = new String(Objects.requireNonNull(SQLib.class.getResourceAsStream("/sqlib.toml")).readAllBytes()).replace("${sqlite_path}", sqliteDataDir.toString());

                try (FileWriter writer = new FileWriter(configFile)) {
                    writer.write(data);
                }
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

            database = new SQLiteDatabase(config.database.name, config.sqlite.directory);
        }
        else if (config.database.type.equalsIgnoreCase("MYSQL")) {
            if (!config.validateMySQL()) {
                log(Level.ERROR, "Invalid config - Stopping");
                System.exit(1);
            }

            database = new MySQLDatabase(config.database.name, config.mysql.address, String.valueOf(config.mysql.port), config.mysql.username, config.mysql.password);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> databases.forEach(Database::close)));
    }

    public static void registerDatabase(Database database) {
        databases.add(database);
    }

    public static List<Database> getAllDatabases() {
        return databases.stream().toList();
    }

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }
}