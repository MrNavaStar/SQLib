package me.mrnavastar.sqlib.api.database;

import me.mrnavastar.sqlib.impl.SQLPrimitive;

public class PostgreSQL extends AuthenticatedDatabase {

    public PostgreSQL(String name, String address, String port, String username, String password) {
        super(name, address, port, username, password);
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:postgresql://%s:%s/%s".formatted(address, port, name);
    }

    @Override
    public String getTableCreationQuery(String tableName) {
        return "CREATE TABLE IF NOT EXISTS %s (SQLIB_AUTO_ID BIGSERIAL PRIMARY KEY)".formatted(tableName);
    }

    @Override
    public String getRowCreationQuery(String rowName) {
        return "INSERT INTO %s VALUES(DEFAULT) RETURNING SQLIB_AUTO_ID".formatted(rowName);
    }

    @Override
    public String getColumnListQuery(String tableName) {
        return "SELECT * FROM %s WHERE 1 = 0".formatted(tableName);
    }

    @Override
    public String getDataType(SQLPrimitive<?> type) {
        return switch (type.getType()) {
            case BYTE, BOOL -> "TINYINT";
            case BYTES -> "BYTEA";
            case SHORT -> "SMALLINT";
            case INT -> "INT";
            case DOUBLE -> "DOUBLE PRECISION";
            case LONG -> "BIGINT";
            case STRING -> "TEXT";
            case CHAR -> "CHAR(2)";
            default -> type.getType().name();
        };
    }
}
