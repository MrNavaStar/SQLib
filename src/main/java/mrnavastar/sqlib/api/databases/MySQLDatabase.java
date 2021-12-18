package mrnavastar.sqlib.api.databases;

import mrnavastar.sqlib.util.SqlManager;

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
        this.connect();
        this.disconnect();
    }

    public String getType() {
        return "mysql";
    }

    @Override
    public void connect() {
        SqlManager.connectMYSQL(this.address, this.port, this.name, this.username, this.password);
    }

    @Override
    public void beginTransaction() {
        SqlManager.beginTransaction(false);
    }
}