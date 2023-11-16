package me.mrnavastar.sqlib.database;

import lombok.Getter;

import java.util.Properties;

@Getter
public abstract class AuthenticatedDatabase extends Database {

    protected final String address;
    protected final String port;
    protected final String username;
    protected final String password;

    public AuthenticatedDatabase(String name, String address, String port, String username, String password) {
        super(name);
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
        open();
    }

    @Override
    public Properties getConnectionProperties() {
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        return properties;
    }

    @Override
    public void beginTransaction() {
        sqlConnection.beginTransaction(false);
    }
}
