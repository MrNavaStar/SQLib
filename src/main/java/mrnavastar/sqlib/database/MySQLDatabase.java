package mrnavastar.sqlib.database;

import java.util.Properties;

public class MySQLDatabase extends Database {

    private final String address;
    private final String port;
    private final String username;
    private final String password;

    public MySQLDatabase(String name, String address, String port, String username, String password) {
        super(name);
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
        open();
    }


    @Override
    public String getConnectionUrl() {
        return "jdbc:mysql://" + address + ":" + port + "/" + name;
    }

    @Override
    public Properties getConnectionProperties() {
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        return properties;
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns) {
        return "CREATE TABLE IF NOT EXISTS %s (ID TEXT, %s PRIMARY KEY (ID(256)));".formatted(tableName, columns);
    }

    @Override
    public void beginTransaction() {
        sqlConnection.beginTransaction(false);
    }
}