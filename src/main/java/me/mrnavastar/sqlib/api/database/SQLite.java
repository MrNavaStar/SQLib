package me.mrnavastar.sqlib.api.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.impl.SQLPrimitive;

import java.io.File;

@Getter
public class SQLite extends Database {

    private final String directory;
    private Mode mode = Mode.WAL2;

    public enum Mode {
        DELETE,
        TRUNCATE,
        PERSIST,
        MEMORY,
        WAL,
        WAL2,
        OFF
    }

    public SQLite(@NonNull String name, @NonNull String directory) {
        super(name);
        loadDriver("org.sqlite.JDBC");
        this.directory = directory;
        connect();
        connection.getSql().useHandle(h -> h.execute("PRAGMA journal_mode = %s;".formatted(mode)));
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:sqlite:" + new File(directory + "/" + name + ".db");
    }

    @Override
    public String getTableCreationQuery(String tableName) {
        return "CREATE TABLE IF NOT EXISTS %s (SQLIB_AUTO_ID INTEGER PRIMARY KEY UNIQUE)".formatted(tableName);
    }

    @Override
    public String getDataType(SQLPrimitive<?> type) {
        return switch (type.getType()) {
            default -> type.getType().name();

            case BYTE, BOOL -> "TINYINT";
            case BYTES -> "BLOB";
            case SHORT -> "SMALLINT";
            case LONG -> "BIGINT";
            case STRING -> "TEXT";
            case CHAR -> "CHARACTER";
        };
    }

    @SneakyThrows
    public void setMode(@NonNull Mode mode) {
        this.mode = mode;
        connection.getSql().useHandle(h -> h.execute("PRAGMA journal_mode = %s;".formatted(mode)));
    }
}