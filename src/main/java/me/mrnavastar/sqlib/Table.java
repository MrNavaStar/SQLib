package me.mrnavastar.sqlib;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.database.Database;
import me.mrnavastar.sqlib.sql.SQLConnection;
import me.mrnavastar.sqlib.sql.SQLDataType;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class acts as a wrapper for a table in a {@link Database}
 */
public class Table {

    @Getter
    private final String modId;
    @Getter
    private final String name;
    @Getter
    private final Database database;
    private final SQLConnection connection;
    private final HashMap<String, SQLDataType> columns;

    @Getter
    private boolean isInTransaction = false;
    private final boolean autoIncrement;

    public Table(@NonNull String modId, @NonNull String name, @NonNull Database database, HashMap<String, SQLDataType> columns, @NonNull SQLConnection connection, boolean autoIncrement) {
        this.modId = modId;
        this.name = name;
        this.database = database;
        this.columns = columns;
        this.connection = connection;
        this.autoIncrement = autoIncrement;
    }

    public String getNoConflictName() {
        return modId + "_" + name;
    }

    public HashMap<String, SQLDataType> getColumns() {
        return (HashMap<String, SQLDataType>) columns.clone();
    }

    /**
     * Create a transactional lock on this database. No data will be written until you call {@link Table#endTransaction()}
     */
    @SneakyThrows
    public void beginTransaction() {
        if (!isInTransaction) {
            database.beginTransaction();
            isInTransaction = true;
        }
    }

    /**
     * Close the transactional lock on this database and write the data.
     */
    @SneakyThrows
    public void endTransaction() {
        database.endTransaction();
        isInTransaction = false;
    }

    /**
     * @return A list of all the {@link DataContainer} ids in this table.
     */
    @SneakyThrows
    public List<String> getIds() {
        return connection.listPrimaryKeys(this);
    }

    /**
     * @return A list of all the {@link DataContainer} ids in this table.
     */
    public List<UUID> getIdsAsUUIDs() {
        return getIds().stream().map(UUID::fromString).toList();
    }

    /**
     * @return A list of all the {@link DataContainer} ids in this table.
     */
    public List<Integer> getIdsAsInts() {
        return getIds().stream().map(Integer::parseInt).toList();
    }

    /**
     * Creates a new {@link DataContainer}
     */
    @SneakyThrows
    public DataContainer createDataContainer(@NonNull String id) {
        if (!autoIncrement) {
            DataContainer dataContainer = get(id);
            if (dataContainer != null) this.drop(dataContainer);
        }

        int autoId = connection.createRow(this, id, autoIncrement);
        if (autoIncrement) id = String.valueOf(autoId);

        return new DataContainer(id, this, connection);
    }

    /**
     * Creates a new {@link DataContainer}
     */
    public DataContainer createDataContainer(@NonNull UUID id) {
       return createDataContainer(id.toString());
    }

    /**
     * Creates a new {@link DataContainer}
     */
    public DataContainer createDataContainer(int id) {
       return createDataContainer(String.valueOf(id));
    }

    /**
     * Tries to get a {@link DataContainer} or creates a new {@link DataContainer} if it is missing
     */
    public DataContainer getOrCreateDataContainer(@NonNull String id) {
        DataContainer dataContainer = get(id);
        return dataContainer == null ? createDataContainer(id) : dataContainer;
    }

    /**
     * Tries to get a {@link DataContainer} or creates a new {@link DataContainer} if it is missing
     */
    public DataContainer getOrCreateDataContainer(@NonNull UUID id) {
        return getOrCreateDataContainer(id.toString());
    }

    /**
     * Tries to get a {@link DataContainer} or creates a new {@link DataContainer} if it is missing
     */
    public DataContainer getOrCreateDataContainer(int id) {
        return getOrCreateDataContainer(String.valueOf(id));
    }

    /**
    * Creates a new row in the database and creates a handy wrapper class
    * @return DataContainer or null if the table is not configured for autoIncrementing.
    */
    public DataContainer createDataContainerAutoID() {
        if (!autoIncrement) return null;
        return createDataContainer("");
    }

    /**
     * Delete the {@link DataContainer} from the database
     * @param dataContainer The {@link DataContainer} to delete
     */
    @SneakyThrows
    public void drop(@NonNull DataContainer dataContainer) {
        if (contains(dataContainer.getIdAsString())) connection.deleteRow(this, dataContainer.getIdAsString());
    }

    /**
     * Delete the {@link DataContainer} from the database
     * @param id The id of the {@link DataContainer} to delete
     */
    public void drop(@NonNull String id) {
        drop(get(id));
    }

    /**
     * Delete the {@link DataContainer} from the database
     * @param id The id of the {@link DataContainer} to delete
     */
    public void drop(@NonNull UUID id) {
        drop(id.toString());
    }

    /**
     * Delete the {@link DataContainer} from the database
     * @param id The id of the {@link DataContainer} to delete
     */
    public void drop(int id) {
        drop(String.valueOf(id));
    }

    /**
     * Tries to get a {@link DataContainer}
     * @return The {@link DataContainer} or null if it is missing
     */
    public DataContainer get(@NonNull String id) {
        if (contains(id)) return new DataContainer(id, this, connection);
        return null;
    }

    /**
     * Tries to get a {@link DataContainer}
     * @return The {@link DataContainer} or null if it is missing
     */
    public DataContainer get(@NonNull UUID id) {
        return get(id.toString());
    }

    /**
     * Tries to get a {@link DataContainer}
     * @return The {@link DataContainer} or null if it is missing
     */
    public DataContainer get(int id) {
        return get(String.valueOf(id));
    }

    /**
     * Checks if the table contains a {@link DataContainer}
     * @param id The id of the {@link DataContainer} to look for
     */
    @SneakyThrows
    public boolean contains(@NonNull String id) {
        return connection.rowExists(this, id);
    }

    /**
     * Checks if the table contains a {@link DataContainer}
     * @param id The id of the {@link DataContainer} to look for
     */
    public boolean contains(@NonNull UUID id) {
        return contains(id.toString());
    }

    /**
     * Checks if the table contains a {@link DataContainer}
     * @param id The id of the {@link DataContainer} to look for
     */
    public boolean contains(int id) {
        return contains(String.valueOf(id));
    }

    /**
     * @return A list of all the {@link DataContainer}'s in this table
     */
    @SneakyThrows
    public List<DataContainer> getDataContainers() {
        return connection.listPrimaryKeys(this).stream().map(id -> new DataContainer(id, this, connection)).toList();
    }
}