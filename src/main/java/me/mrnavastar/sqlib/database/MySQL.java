package me.mrnavastar.sqlib.database;

import me.mrnavastar.sqlib.sql.SQLDataType;

public class MySQL extends AuthenticatedDatabase {

    public MySQL(String name, String address, String port, String username, String password) {
        super(name, address, port, username, password);
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:mariadb://%s:%s/%s".formatted(address, port, name);
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns, boolean autoIncrementId) {
        if (autoIncrementId) return "CREATE TABLE IF NOT EXISTS %s (ID int, %s, PRIMARY KEY (ID) AUTO_INCREMENT);".formatted(tableName, columns);
        return "CREATE TABLE IF NOT EXISTS %s (ID TEXT, %s, PRIMARY KEY (ID(256)));".formatted(tableName, columns);
    }

    @Override
    public String getTransactionString() {
        return "BEGIN;";
    }

    @Override
    public String getDataType(SQLDataType dataType) {
        return switch (dataType) {
            case STRING, TEXT, JSON, NBT, IDENTIFIER -> "LONGTEXT";
            case BYTES -> "LONGBLOB";
            case INT, COLOR -> "INT(255)";
            case DOUBLE -> "FLOAT(53)";
            case LONG, DATE, BLOCKPOS, CHUNKPOS -> "BIGINT(255)";
            case BOOL -> "INT(1)";
            case UUID -> "CHAR(36)";
        };
    }
}