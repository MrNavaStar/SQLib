package me.mrnavastar.sqlib.config;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import me.mrnavastar.sqlib.SQLib;
import me.mrnavastar.sqlib.api.database.MySQL;
import me.mrnavastar.sqlib.api.database.PostgreSQL;
import me.mrnavastar.sqlib.api.database.SQLite;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class Config {

    public static Config INSTANCE;

    public Database database;
    public Local local;
    public Server server;

    public static class Database {
        public String name;
        public String type;
        public int timeout;

        public boolean validate() {
            return name != null && !name.isEmpty() && type != null && !type.isEmpty() && timeout > 0;
        }
    }

    public static class Local {
        public String directory;

        public boolean validate() {
            return directory != null && !directory.isEmpty() && !directory.startsWith("/") && new File(directory).exists();
        }
    }

    public static class Server {
        public String address;
        public int port = -1;
        public String username;
        public String password;

        public boolean validate() {
            return address != null && !address.isEmpty() && port != -1 && username != null && !username.isEmpty() && password != null && !password.isEmpty();
        }
    }

    public boolean validate() {
        if (database == null || !database.validate()) return false;

        if (database.type.equalsIgnoreCase("sqlite") && local.validate()) return true;
        if (database.type.equalsIgnoreCase("mysql") && server.validate()) return true;
        if (database.type.equalsIgnoreCase("mariadb") && server.validate()) return true;
        return database.type.equalsIgnoreCase("postgres") && server.validate();
    }

    public static me.mrnavastar.sqlib.api.database.Database load(Path localDir, Path configDir) {
        INSTANCE = new Config();
        try {
            File configFile = new File(configDir + "/sqlib.toml");
            if (!configFile.exists()) {
                String data = new String(Objects.requireNonNull(SQLib.class.getResourceAsStream("/sqlib.toml")).readAllBytes()).replace("${local_path}", localDir.toString());
                try (FileWriter writer = new FileWriter(configFile)) {
                    writer.write(data);
                }
            }
            INSTANCE = new TomlMapper().readValue(configFile, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!INSTANCE.validate()) {
            SQLib.log(Level.ERROR, "Invalid config - Stopping");
            System.exit(1);
        }

        return switch (INSTANCE.database.type.toLowerCase()) {
            case "sqlite" -> new SQLite(INSTANCE.database.name, INSTANCE.local.directory);
            case "mysql", "mariadb" -> new MySQL(INSTANCE.database.name, INSTANCE.server.address, String.valueOf(INSTANCE.server.port), INSTANCE.server.username, INSTANCE.server.password);
            case "postgres" -> new PostgreSQL(INSTANCE.database.name, INSTANCE.server.address, String.valueOf(INSTANCE.server.port), INSTANCE.server.username, INSTANCE.server.password);
            default -> null;
        };
    }
}