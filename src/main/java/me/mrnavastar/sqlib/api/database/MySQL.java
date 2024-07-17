package me.mrnavastar.sqlib.api.database;

import lombok.SneakyThrows;
import me.mrnavastar.sqlib.impl.SQLPrimitive;

public class MySQL extends AuthenticatedDatabase {

    @SneakyThrows
    public MySQL(String name, String address, String port, String username, String password) {
        super(name, address, port, username, password);
        Class.forName("me.mrnavastar.sqlib.libs.org.mariadb.jdbc.Driver");
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:mariadb://%s:%s/%s".formatted(address, port, name);
    }

    @Override
    public String getTableCreationQuery(String tableName) {
        return "CREATE TABLE IF NOT EXISTS %s (SQLIB_AUTO_ID INT AUTO_INCREMENT, PRIMARY KEY (SQLIB_AUTO_ID))".formatted(tableName);
    }

    @Override
    public String getRowCreationQuery(String rowName) {
        return "INSERT INTO %s () VALUES () RETURNING SQLIB_AUTO_ID".formatted(rowName);
    }

    @Override
    public String getDataType(SQLPrimitive<?> type) {
        return switch (type.getType()) {
            default -> type.getType().name();

            case BYTE, BOOL -> "TINYINT";
            case BYTES -> "LONGBLOB";
            case SHORT -> "SMALLINT";
            case LONG -> "BIGINT";
            case STRING -> "LONGTEXT";
            case CHAR -> "CHAR(2)";
        };
    }
}