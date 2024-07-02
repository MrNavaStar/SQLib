package me.mrnavastar.sqlib.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;

@Getter
public class SQLiteDatabase extends Database {

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
    public SQLiteDatabase(@NonNull String name, @NonNull String directory) {
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
        return "BEGIN EXCLUSIVE;";
    }

    @SneakyThrows
    public void setMode(@NonNull Mode mode) {
        this.mode = mode;
        executeCommand("PRAGMA journal_mode = %s;".formatted(mode)).close();
    }
}