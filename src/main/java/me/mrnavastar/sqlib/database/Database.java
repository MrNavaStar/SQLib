package me.mrnavastar.sqlib.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.mrnavastar.sqlib.SQLib;
import me.mrnavastar.sqlib.Table;
import me.mrnavastar.sqlib.TableBuilder;
import me.mrnavastar.sqlib.sql.SQLConnection;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class can be extended to allow for new database implementations
 */
@RequiredArgsConstructor
public abstract class Database {

    @Getter
    @NonNull
    protected final String name;
    private final ConcurrentHashMap<String, Table> tables = new ConcurrentHashMap<>();
    protected SQLConnection connection;

    public abstract String getConnectionUrl();

    public Properties getConnectionProperties() {
        return new Properties();
    }

    public abstract String getTableCreationQuery(String tableName, String columns, boolean autoIncrementId);

    public void open() {
        if (connection == null) {
            connection = new SQLConnection(getConnectionUrl(), getConnectionProperties());
            SQLib.registerDatabase(this);
        }
    }

    public void close() {
        if (connection != null) connection.close();
        connection = null;
    }

    public abstract String getTransactionString();

    public void beginTransaction() {
        connection.beginTransaction(getTransactionString());
    };

    public void endTransaction() {
        connection.endTransaction();
    }

    public TableBuilder createTable(String modId, String name) {
        return new TableBuilder(modId, name, this, connection);
    }

    public void addTable(Table table) {
        tables.put(table.getNoConflictName(), table);
    }

    public Table getTable(String modId, String name) {
        return tables.get(modId + "_" + name);
    }

    public ArrayList<Table> getTables() {
        return new ArrayList<>(tables.values());
    }

    public PreparedStatement executeCommand(String sql, boolean autoClose, Object... params) {
        return connection.executeCommand(sql, autoClose, params);
    }
}