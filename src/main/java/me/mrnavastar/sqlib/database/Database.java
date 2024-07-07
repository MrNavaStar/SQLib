package me.mrnavastar.sqlib.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.SQLib;
import me.mrnavastar.sqlib.api.Table;
import me.mrnavastar.sqlib.sql.ColumnType;
import me.mrnavastar.sqlib.sql.Column;
import me.mrnavastar.sqlib.sql.SQLConnection;
import me.mrnavastar.sqlib.sql.SQLPrimitives;

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
        private final HashMap<String, Column> columns = new HashMap<>();

        public TableBuilder(@NonNull String modId, @NonNull String name, @NonNull Database database) {
            this.modId = modId;
            this.name = name;
            this.database = database;
        }

        /**
         * Adds a column to the table definition
         * @param name The name of the column
         * @param dataType The {@link ColumnType} of this column
         */
        public TableBuilder column(@NonNull String name, @NonNull ColumnType<?> dataType) {
            if (name.equalsIgnoreCase("SQLIB_AUTO_ID")) throw new RuntimeException("Invalid column! SQLIB_AUTO_ID is used internally!");
            columns.put(name, new Column(name, dataType, false, false));
            return this;
        }

        /**
         * Adds a column to the table definition
         * @param name The name of the column
         * @param dataType The {@link ColumnType} of this column
         */
        public TableBuilder uniqueColumn(@NonNull String name, @NonNull ColumnType dataType) {
            if (name.equalsIgnoreCase("SQLIB_AUTO_ID")) throw new RuntimeException("Invalid column! SQLIB_AUTO_ID is used internally!");
            columns.put(name, new Column(name, dataType, false, true));
            return this;
        }

        /**
         * Adds a column to the table definition
         * @param name The name of the column
         * @param dataType The {@link ColumnType} of this column
         */
        public TableBuilder arrayColumn(@NonNull String name, @NonNull ColumnType dataType) {
            if (name.equalsIgnoreCase("SQLIB_AUTO_ID")) throw new RuntimeException("Invalid column! SQLIB_AUTO_ID is used internally!");
            columns.put(name, new Column(name, dataType, true, true));
            return this;
        }

        /**
         * Call this function when you are done configuring your table.
         * @return The finished table
         */
        @SneakyThrows
        public Table finish() {
            Table table = new Table(modId, name, database, columns, database.connection);
            database.connection.createTable(table);
            database.tables.put(table.getNoConflictName(), table);
            return table;
        }
    }

    public abstract String getConnectionUrl();

    public Properties getConnectionProperties() {
        return new Properties();
    }

    public abstract String getTableCreationQuery(String tableName, String columns);

    public String getTransactionString() {
        return "BEGIN;";
    };

    public abstract String getDataType(SQLPrimitives<?> dataType);

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

    public TableBuilder createTable(String modId, String name) {
        return new TableBuilder(modId, name, this);
    }

    public Table getTable(String modId, String name) {
        return tables.get(modId + "_" + name);
    }

    public ArrayList<Table> getTables() {
        return new ArrayList<>(tables.values());
    }
}