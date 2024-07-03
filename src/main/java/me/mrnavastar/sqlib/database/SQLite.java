package me.mrnavastar.sqlib.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.sql.SQLDataType;

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
    public String getTableCreationQuery(String tableName, String columns, boolean autoIncrementId) {
        if (autoIncrementId) return "CREATE TABLE IF NOT EXISTS %s (%s, ID INTEGER PRIMARY KEY AUTOINCREMENT);".formatted(tableName, columns);
        return "CREATE TABLE IF NOT EXISTS %s (%s, ID MEDIUMTEXT PRIMARY KEY);".formatted(tableName, columns);
    }

    @Override
    public String getTransactionString() {
        return "BEGIN;";
    }

    @Override
    public String getDataType(SQLDataType dataType) {
        return switch (dataType) {
            case STRING, TEXT, JSON, NBT, IDENTIFIER -> "LONGTEXT";
            case BYTES -> "BLOB";
            case INT, COLOR -> "INT(255)";
            case DOUBLE -> "FLOAT(53)";
            case LONG, DATE, BLOCKPOS, CHUNKPOS -> "BIGINT(255)";
            case BOOL -> "INT(1)";
            case UUID -> "CHAR(36)";
        };
    }

    @SneakyThrows
    public void setMode(@NonNull Mode mode) {
        this.mode = mode;
        executeCommand("PRAGMA journal_mode = %s;".formatted(mode)).close();
    }
}