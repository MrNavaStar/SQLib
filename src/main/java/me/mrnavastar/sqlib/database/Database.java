package me.mrnavastar.sqlib.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.mrnavastar.sqlib.SQLib;
import me.mrnavastar.sqlib.Table;
import me.mrnavastar.sqlib.sql.SQLConnection;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class can be extended to allow for new database implementations
 */
@RequiredArgsConstructor
public abstract class Database {

    @Getter
    @NonNull
    protected final String name;
    @Getter
    @NonNull
    protected final String modId;
    private final HashMap<String, Table> tables = new HashMap<>();
    protected SQLConnection sqlConnection;

    public abstract String getConnectionUrl();

    public Properties getConnectionProperties() {
        return new Properties();
    }

    public abstract String getTableCreationQuery(String tableName, String columns, boolean autoIncrementId);

    public void open() {
        if (sqlConnection == null) {
            sqlConnection = new SQLConnection(getConnectionUrl(), getConnectionProperties());
            SQLib.registerDatabase(modId, name, this);
        }
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
        return tables.get(modId + "_" + name);
    }

    public ArrayList<Table> getTables() {
        return new ArrayList<>(tables.values());
    }

    public PreparedStatement executeCommand(String sql, boolean autoClose, Object... params) {
        return sqlConnection.executeCommand(sql, autoClose, params);
    }
}