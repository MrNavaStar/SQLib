package mrnavastar.sqlib.api.databases;

import mrnavastar.sqlib.api.Table;
import mrnavastar.sqlib.util.SqlManager;

import java.util.ArrayList;

public abstract class Database {

    protected final String name;
    private static final ArrayList<Table> tables = new ArrayList<>();

    public Database(String name) {
        this.name = name;
        connect();
        disconnect();
    }

    public abstract void connect();

    public void disconnect() {
        SqlManager.disconnect();
    }

    public Table createTable(String name) {
        Table table = new Table(name);
        addTable(table);
        return table;
    }

    public void addTable(Table table) {
        if (!tables.contains(table)) {
            table.addToDatabase(this);
            tables.add(table);
        }
    }
}
