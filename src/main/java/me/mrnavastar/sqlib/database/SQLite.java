package me.mrnavastar.sqlib.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.sql.SQLPrimitives;

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

    @SneakyThrows
    public SQLite(@NonNull String name, @NonNull String directory) {
        super(name);
        this.directory = directory;
        open();
        executeCommand("PRAGMA journal_mode = %s;".formatted(mode)).close();
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:sqlite:" + new File(directory + "/" + name + ".db");
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns) {
        return "CREATE TABLE IF NOT EXISTS %s (SQLIB_AUTO_ID INTEGER PRIMARY KEY UNIQUE, %s);".formatted(tableName, columns);
    }

    @Override
    public String getDataType(SQLPrimitives<?> type) {
        return switch (type.getType()) {
            default -> type.getType().name();

            case BYTE -> "TINYINT";
            case BYTES -> "BLOB";
            case BOOL -> "BOOLEAN";
            case SHORT -> "SMALLINT";
            case LONG -> "BIGINT";
            case STRING -> "TEXT";
            case CHAR -> "CHARACTER";
        };
    }

    @SneakyThrows
    public void setMode(@NonNull Mode mode) {
        this.mode = mode;
        executeCommand("PRAGMA journal_mode = %s;".formatted(mode)).close();
    }
}