package me.mrnavastar.sqlib.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.SQLib;
import me.mrnavastar.sqlib.Table;
import me.mrnavastar.sqlib.sql.SQLConnection;
import me.mrnavastar.sqlib.sql.SQLDataType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static class TableBuilder {

        private final String modId;
        private final String name;
        private final Database database;
        private final HashMap<String, SQLDataType> columns = new HashMap<>();
        protected boolean autoIncrement = false;


        public TableBuilder(@NonNull String modId, @NonNull String name, @NonNull Database database) {
            this.modId = modId;
            this.name = name;
            this.database = database;
        }

        /**
         * Make this table use auto incremented ids. See {@link Table#createDataContainerAutoID()}
         */
        public TableBuilder setAutoIncrement() {
            autoIncrement = true;
            return this;
        }

        /**
         * Adds a column to the table definition
         * @param name The name of the column
         * @param dataType The {@link SQLDataType} of this column
         */
        public TableBuilder addColumn(@NonNull String name, @NonNull SQLDataType dataType) {
            columns.put(name, dataType);
            return this;
        }

        /**
         * Call this function when you are done configuring your table.
         * @return The finished table
         */
        @SneakyThrows
        public Table finish() {
            Table table = new Table(modId, name, database, columns, database.connection, autoIncrement);
            database.connection.createTable(table, autoIncrement);
            database.tables.put(table.getNoConflictName(), table);
            return table;
        }
    }

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

    public void beginTransaction() throws SQLException {
        connection.beginTransaction(getTransactionString());
    };

    public void endTransaction() throws SQLException {
        connection.endTransaction();
    }

    public TableBuilder createTable(String modId, String name) {
        return new TableBuilder(modId, name, this);
    }

    public Table getTable(String modId, String name) {
        return tables.get(modId + "_" + name);
    }

    public ArrayList<Table> getTables() {
        return new ArrayList<>(tables.values());
    }

    public PreparedStatement executeCommand(String sql, Object... params) throws SQLException {
        return connection.executeCommand(sql, params);
    }
}