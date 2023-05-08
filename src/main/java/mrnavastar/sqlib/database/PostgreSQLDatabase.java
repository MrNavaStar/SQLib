package mrnavastar.sqlib.database;

public class PostgreSQLDatabase extends AuthenticatedDatabase {

    public PostgreSQLDatabase(String modId, String name, String address, String port, String username, String password) {
        super(modId, name, address, port, username, password);
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:postgresql://" + address + ":" + port + "/" + name;
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns) {
        return "CREATE TABLE [IF NOT EXISTS] %s (ID TEXT PRIMARY KEY, %s);".formatted(tableName, columns);
    }
}