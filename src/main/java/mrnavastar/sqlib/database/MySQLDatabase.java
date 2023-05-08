package mrnavastar.sqlib.database;

public class MySQLDatabase extends AuthenticatedDatabase {

    public MySQLDatabase(String modId, String name, String address, String port, String username, String password) {
        super(modId, name, address, port, username, password);
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:mysql://" + address + ":" + port + "/" + name;
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns) {
        return "CREATE TABLE IF NOT EXISTS %s (ID TEXT, %s PRIMARY KEY (ID(256)));".formatted(tableName, columns);
    }
}