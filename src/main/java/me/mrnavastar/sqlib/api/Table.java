package me.mrnavastar.sqlib.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.api.database.Database;
import me.mrnavastar.sqlib.impl.Column;
import me.mrnavastar.sqlib.impl.SQLConnection;

import java.util.*;

/**
 * This class represents a table in a {@link Database}.
 */
public class Table {

    @Getter
    private final String modId;
    @Getter
    private final String name;
    @Getter
    private final Database database;
    private final SQLConnection connection;
    private final HashMap<String, Column> columns;

    public Table(@NonNull String modId, @NonNull String name, @NonNull Database database, HashMap<String, Column> columns, @NonNull SQLConnection connection) {
        this.modId = modId;
        this.name = name;
        this.database = database;
        this.columns = columns;
        this.connection = connection;
    }

    public String getNoConflictName() {
        return modId + "_" + name;
    }

    public Map<String, Column> getColumns() {
        return Collections.unmodifiableMap(columns);
    }

    /**
     * Creates a new {@link DataContainer}
     */
    @SneakyThrows
    public DataContainer createDataContainer() {
        return new DataContainer(this, connection.createRow(this), connection);
    }

    /**
     * Tries to get a {@link DataContainer} or creates a new {@link DataContainer} if it is missing.
     * Note that the created {@link DataContainer} is not guaranteed to have the same id as the one passed in.
     */
    public DataContainer getOrCreateDataContainer(int id) {
        DataContainer dataContainer = getDataContainer(id);
        return dataContainer != null ? dataContainer : createDataContainer();
    }

    /**
     * @return A {@link DataContainer}'s with a matching unique key value pair or null if one does not exist.
     */
    @SneakyThrows
    public DataContainer getDataContainer(@NonNull String field, @NonNull Object value) {
        return getDataContainers(field, value).stream().findFirst().orElse(null);
    }

    /**
     * @return A {@link DataContainer} or null if it is missing.
     */
    @SneakyThrows
    public DataContainer getDataContainer(int id) {
        if (!connection.rowExists(this, id)) return null;
        return new DataContainer(this, id, connection);
    }

    /**
     * @return A list of all the {@link DataContainer}'s in this table.
     */
    @SneakyThrows
    public List<DataContainer> getDataContainers() {
        // Done this way to ensure that we always match the true state of the database in the case that the database is modified externally
        return connection.listIds(this).stream().map(id -> new DataContainer(this, id, connection)).toList();
    }

    /**
     * @return A list of all the {@link DataContainer}'s with a matching key value pair.
     */
    @SneakyThrows
    public List<DataContainer> getDataContainers(@NonNull String field, @NonNull Object value) {
        // Done this way to ensure that we always match the true state of the database in the case that the database is modified externally
        return connection.findRows(this, field, value).stream().map(id -> new DataContainer(this, id, connection)).toList();
    }
}