package me.mrnavastar.sqlib.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.api.database.Database;
import me.mrnavastar.sqlib.impl.SQLConnection;

import java.util.*;

/**
 * This class represents a table in a {@link Database}.
 */
public class DataStore {

    @Getter
    private final String modId;
    @Getter
    private final String name;
    @Getter
    private final Database database;
    private final SQLConnection connection;

    public DataStore(@NonNull String modId, @NonNull String name, @NonNull Database database, @NonNull SQLConnection connection) {
        this.modId = modId;
        this.name = name;
        this.database = database;
        this.connection = connection;
        connection.createTable(this);
    }

    @Override
    public String toString() {
        return modId + "_" + name;
    }

    /**
     * Creates a new {@link DataContainer}
     */
    @SneakyThrows
    public DataContainer createContainer() {
        return new DataContainer(this, connection.createRow(this), connection);
    }

    /**
     * Tries to get a {@link DataContainer} or creates a new {@link DataContainer} if it is missing.
     * Note that the created {@link DataContainer} is not guaranteed to have the same id as the one passed in.
     */
    public DataContainer getOrCreateContainer(int id) {
        DataContainer container = getContainer(id);
        return container != null ? container : createContainer();
    }

    /**
     * @return A {@link DataContainer}'s with a matching unique key value pair or null if one does not exist.
     */
    @SneakyThrows
    public DataContainer getContainer(@NonNull String field, @NonNull Object value) {
        return getContainers(field, value).stream().findFirst().orElse(null);
    }

    /**
     * @return A {@link DataContainer} or null if it is missing.
     */
    @SneakyThrows
    public DataContainer getContainer(int id) {
        if (!connection.rowExists(this, id)) return null;
        return new DataContainer(this, id, connection);
    }

    /**
     * @return A list of all the {@link DataContainer}'s in this table.
     */
    @SneakyThrows
    public List<DataContainer> getContainers() {
        // Done this way to ensure that we always match the true state of the database in the case that the database is modified externally
        return connection.listIds(this).stream().map(id -> new DataContainer(this, id, connection)).toList();
    }

    /**
     * @return A list of all the {@link DataContainer}'s with a matching key value pair.
     */
    @SneakyThrows
    public List<DataContainer> getContainers(@NonNull String field, @NonNull Object value) {
        // Done this way to ensure that we always match the true state of the database in the case that the database is modified externally
        return connection.findRows(this, field, value).stream().map(id -> new DataContainer(this, id, connection)).toList();
    }
}