package me.mrnavastar.sqlib.api.database;

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
        connect();
    }

    @Override
    public Properties getConnectionProperties() {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("ssl", "true");
        properties.put("config_timeout", true);
        return properties;
    }
}