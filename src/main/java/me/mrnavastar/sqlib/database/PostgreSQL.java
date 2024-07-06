package me.mrnavastar.sqlib.database;

import me.mrnavastar.sqlib.sql.SQLPrimitives;

public class PostgreSQL extends AuthenticatedDatabase {

    public PostgreSQL(String name, String address, String port, String username, String password) {
        super(name, address, port, username, password);
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:postgresql//%s:%s/%s".formatted(address, port, name);
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns) {
        return "CREATE TABLE IF NOT EXISTS %s (SQLIB_AUTO_ID BIGSERIAL PRIMARY KEY, %s);".formatted(tableName, columns);
    }

    @Override
    public String getDataType(SQLPrimitives<?> type) {
        return switch (type.getType()) {
            default -> type.getType().name();

            case BYTE -> "INT";
            case BYTES -> "BYTEA";
            case SHORT -> "SMALLINT";
            case INT -> "INT";
            case DOUBLE -> "DOUBLE PRECISION";
            case LONG -> "BIGINT";
            case STRING -> "TEXT";
            case CHAR -> "CHAR(1)";
        };
    }
}
