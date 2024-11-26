package me.mrnavastar.sqlib.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.api.database.Database;
import me.mrnavastar.sqlib.api.types.SQLibType;
import me.mrnavastar.sqlib.impl.SQLConnection;

import java.util.*;
import java.util.function.Consumer;

/**
 * This class represents a table in a {@link Database}.
 */
@EqualsAndHashCode
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
     * Creates a new {@link DataContainer} with a unique id.
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
        return getContainer(id).orElseGet(this::createContainer);
    }

    /**
     * Tries to get a {@link DataContainer} or creates a new {@link DataContainer} if it is missing.
     * Note that the created {@link DataContainer} is not guaranteed to have the same id as the one passed in.
     *
     * @param onCreate A function that gets run only when the container is created. This is useful for setting things
     *                 such as a container id or other elements that are only set once.
     */
    public DataContainer getOrCreateContainer(int id, Consumer<DataContainer> onCreate) {
        return getContainer(id).orElseGet(() -> {
            DataContainer newContainer = createContainer();
            onCreate.accept(newContainer);
            return newContainer;
        });
    }

    /**
     * Tries to get a {@link DataContainer} with a matching key value pair or creates a new {@link DataContainer} if it is missing.
     */
    public DataContainer getOrCreateContainer(@NonNull String field, @NonNull Object value) {
        return getContainer(field, value).orElseGet(this::createContainer);
    }

    /**
     * Tries to get a {@link DataContainer} with a matching key value pair or creates a new {@link DataContainer} if it is missing.
     *
     * @param onCreate A function that gets run only when the container is created. This is useful for setting things
     *                 such as a container id or other elements that are only set once.
     */
    public DataContainer getOrCreateContainer(@NonNull String field, @NonNull Object value, Consumer<DataContainer> onCreate) {
        return getContainer(field, value).orElseGet(() -> {
            DataContainer newContainer = createContainer();
            onCreate.accept(newContainer);
            return newContainer;
        });
    }

    /**
     * Tries to get a {@link DataContainer} with a matching key value pair or creates a new {@link DataContainer} with
     * a matching key value pair if it is missing.
     */
    public <T> DataContainer getOrCreateDefaultContainer(@NonNull SQLibType<T> type, @NonNull String field, @NonNull T value) {
        return getOrCreateContainer(field, value, c -> c.put(type, field, value));
    }

    /**
     * @return A {@link DataContainer}'s with a matching key value pair or null if one does not exist.
     */
    @SneakyThrows
    public Optional<DataContainer> getContainer(@NonNull String field, @NonNull Object value) {
        return getContainers(field, value).stream().findFirst();
    }

    /**
     * @return A {@link DataContainer} or null if it is missing.
     */
    @SneakyThrows
    public Optional<DataContainer> getContainer(int id) {
        return connection.rowExists(this, id) ? Optional.of(new DataContainer(this, id, connection)) : Optional.empty();
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

    /**
     * @return A list of every key present in this {@link DataStore}.
     */
    public List<String> getKeys() {
        return connection.listColumns(this);
    }
}