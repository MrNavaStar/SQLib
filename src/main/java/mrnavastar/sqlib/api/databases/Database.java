package mrnavastar.sqlib.api.databases;

import mrnavastar.sqlib.api.Table;
import mrnavastar.sqlib.util.SqlManager;

import java.util.ArrayList;

public abstract class Database {

    protected final String name;
    private final ArrayList<Table> tables = new ArrayList<>();

    public Database(String name) {
        this.name = name;
    }

    public abstract void connect();

    public void disconnect() {
        SqlManager.disconnect();
    }

    public abstract void beginTransaction();

    public void endTransaction() {
        SqlManager.endTransaction();
    }

    public void add(Table table) {
        if (!tables.contains(table)) {
            table.addToDatabase(this);
            tables.add(table);
        }
    }

    public Table createTable(String name) {
        Table table = new Table(name);
        add(table);
        return table;
    }

    public Table getTable(String name) {
        for (Table table : this.tables) {
            if (table.getName().equals(name)) return table;
        }
        return null;
    }

    public ArrayList<Table> getTables() {
        return this.tables;
    }
}