package mrnavastar.sqlib.database;

import mrnavastar.sqlib.Table;
import mrnavastar.sqlib.sql.SQLConnection;

import java.util.ArrayList;
import java.util.Properties;

public abstract class Database {

    protected final String name;
    private final ArrayList<Table> tables = new ArrayList<>();
    protected SQLConnection sqlConnection;

    public Database(String name) {
        this.name = name;
    }

    public abstract String getConnectionUrl();

    public Properties getConnectionProperties() {
        return new Properties();
    }

    public abstract String getTableCreationQuery(String tableName, String columns, String primaryKey, String primaryKeyType);

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

    public SQLConnection getSqlManager() {
        return sqlConnection;
    }

    public Table createTable(String name) {
        return new Table(name, this);
    }

    public void addTable(Table table) {
        sqlConnection.createTable(table);
        tables.add(table);
    }

    public Table getTable(String name) {
        for (Table table : tables) {
            if (table.getName().equals(name)) return table;
        }
        return null;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }
}