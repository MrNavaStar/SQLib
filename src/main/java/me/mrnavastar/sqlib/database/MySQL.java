package me.mrnavastar.sqlib.database;

import me.mrnavastar.sqlib.sql.SQLPrimitives;

public class MySQL extends AuthenticatedDatabase {

    public MySQL(String name, String address, String port, String username, String password) {
        super(name, address, port, username, password);
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:mariadb://%s:%s/%s".formatted(address, port, name);
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns) {
        return "CREATE TABLE IF NOT EXISTS %s (SQLIB_AUTO_ID INT AUTO_INCREMENT UNIQUE, %s, PRIMARY KEY (SQLIB_AUTO_ID));".formatted(tableName, columns);
    }

    @Override
    public String getDataType(SQLPrimitives<?> type) {
        return switch (type.getType()) {
            default -> type.getType().name();

            case BYTE -> "TINYINT";
            case BYTES -> "LONGBLOB";
            case SHORT -> "SMALLINT";
            case LONG -> "BIGINT";
            case STRING -> "LONGTEXT";
            case CHAR -> "CHAR(1)";
        };
    }
}