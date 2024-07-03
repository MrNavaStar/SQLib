package me.mrnavastar.sqlib.database;

import me.mrnavastar.sqlib.sql.SQLDataType;

public class PostgreSQL extends AuthenticatedDatabase {

    public PostgreSQL(String name, String address, String port, String username, String password) {
        super(name, address, port, username, password);
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:postgresql//%s:%s/%s".formatted(address, port, name);
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns, boolean autoIncrementId) {
        if (autoIncrementId) return "CREATE TABLE IF NOT EXISTS %s (ID SERIAL PRIMARY KEY, %s);".formatted(tableName, columns);
        return "CREATE TABLE IF NOT EXISTS %s (ID TEXT PRIMARY KEY, %s);".formatted(tableName, columns);
    }

    @Override
    public String getTransactionString() {
        return "BEGIN;";
    }

    @Override
    public String getDataType(SQLDataType dataType) {
        return switch (dataType) {
            case STRING, TEXT, NBT, IDENTIFIER -> "TEXT";
            case JSON -> "JSON";
            case BYTES -> "BYTEA";
            case INT, COLOR -> "INT";
            case DOUBLE -> "DECIMAL";
            case LONG, DATE, BLOCKPOS, CHUNKPOS -> "BIGINT";
            case BOOL -> "SMALLINT";
            case UUID -> "UUID";
        };
    }
}
