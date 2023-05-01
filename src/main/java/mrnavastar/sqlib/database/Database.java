package mrnavastar.sqlib.database;

import mrnavastar.sqlib.Table;
import mrnavastar.sqlib.sql.SQLConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public abstract class Database {

    protected final String name;
    private final String modId;
    private final HashMap<String, Table> tables = new HashMap<>();
    protected SQLConnection sqlConnection;

    public Database(String modId, String name) {
        this.modId = modId;
        this.name = name;
    }

    public abstract String getConnectionUrl();

    public Properties getConnectionProperties() {
        return new Properties();
    }

    public abstract String getTableCreationQuery(String tableName, String columns);

    public void open() {
        if (sqlConnection == null) sqlConnection = new SQLConnection(getConnectionUrl(), getConnectionProperties());
    }

    public void close() {
        if (sqlConnection != null) sqlConnection.close();
        sqlConnection = null;
    }

    public abstract void beginTransaction();

    public void endTransaction() {
        sqlConnection.endTransaction();
    }

    public Table createTable(String name) {
        return new Table(modId, name, this, sqlConnection);
    }

    public void addTable(Table table) {
        tables.put(table.getName(), table);
    }

    public Table getTable(String name) {
        return tables.get(modId + ":" + name);
    }

    public ArrayList<Table> getTables() {
        return (ArrayList<Table>) tables.values();
    }
}