package me.mrnavastar.sqlib.database;

import lombok.Getter;
import lombok.NonNull;

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

    public SQLiteDatabase(@NonNull String modId, @NonNull String name, @NonNull String directory) {
        super(modId, name);
        this.directory = directory;
        open();
        executeCommand("PRAGMA journal_mode = %s;".formatted(mode), true);
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

    public void setMode(@NonNull Mode mode) {
        this.mode = mode;
        executeCommand("PRAGMA journal_mode = %s;".formatted(mode), true);
    }

    @Override
    public void beginTransaction() {
        sqlConnection.beginTransaction(true);
    }
}