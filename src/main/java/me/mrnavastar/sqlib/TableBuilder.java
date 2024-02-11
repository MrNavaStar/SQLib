package me.mrnavastar.sqlib;

import lombok.NonNull;
import me.mrnavastar.sqlib.database.Database;
import me.mrnavastar.sqlib.sql.SQLConnection;
import me.mrnavastar.sqlib.sql.SQLDataType;

import java.util.HashMap;

public class TableBuilder {

    private final String modId;
    private final String name;
    private final Database database;
    private final SQLConnection sqlConnection;
    private final HashMap<String, SQLDataType> columns = new HashMap<>();
    protected boolean autoIncrement = false;


    public TableBuilder(@NonNull String modId, @NonNull String name, @NonNull Database database, @NonNull SQLConnection sqlConnection) {
        this.modId = modId;
        this.name = name;
        this.database = database;
        this.sqlConnection = sqlConnection;
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
    public Table finish() {
        return new Table(modId, name, database, sqlConnection, autoIncrement);
    }
}