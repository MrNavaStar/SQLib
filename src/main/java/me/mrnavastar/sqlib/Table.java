package me.mrnavastar.sqlib;

import lombok.Getter;
import lombok.NonNull;
import me.mrnavastar.sqlib.database.Database;
import me.mrnavastar.sqlib.sql.SQLConnection;

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
    private final HashMap<String, DataContainer> dataContainers = new HashMap<>();

    @Getter
    private boolean isInTransaction = false;
    private final boolean autoIncrement;

    public Table(@NonNull String modId, @NonNull String name, @NonNull Database database, @NonNull SQLConnection connection, boolean autoIncrement) {
        this.modId = modId;
        this.name = name;
        this.database = database;
        this.connection = connection;
        this.autoIncrement = autoIncrement;

        connection.createTable(this, autoIncrement);
        database.addTable(this);
        connection.listPrimaryKeys(this).forEach(key -> dataContainers.put(key, new DataContainer(key, this, connection)));
    }

    public String getNoConflictName() {
        return modId + "_" + name;
    }

    /**
     * Create a transactional lock on this database. No data will be written until you call {@link Table#endTransaction()}
     */
    public void beginTransaction() {
        if (!isInTransaction) {
            database.beginTransaction();
            isInTransaction = true;
        }
    }

    /**
     * Close the transactional lock on this database and write the data.
     */
    public void endTransaction() {
        database.endTransaction();
        isInTransaction = false;
    }

    /**
     * @return A list of all the {@link DataContainer} ids in this table.
     */
    public List<String> getIds() {
        return dataContainers.keySet().stream().toList();
    }

    /**
     * @return A list of all the {@link DataContainer} ids in this table.
     */
    public List<UUID> getIdsAsUUIDs() {
        return dataContainers.keySet().stream().map(UUID::fromString).toList();
    }

    /**
     * @return A list of all the {@link DataContainer} ids in this table.
     */
    public List<Integer> getIdsAsInts() {
        return dataContainers.keySet().stream().map(Integer::parseInt).toList();
    }

    /**
     * Creates a new {@link DataContainer}
     */
    public DataContainer createDataContainer(@NonNull String id) {
        if (!autoIncrement) {
            DataContainer dataContainer = get(id);
            if (dataContainer != null) this.drop(dataContainer);
        }

        int autoId = connection.createRow(this, id, autoIncrement);
        if (autoIncrement) id = String.valueOf(autoId);

        DataContainer dataContainer = new DataContainer(id, this, connection);
        dataContainers.put(id, dataContainer);
        return dataContainer;
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
    public void drop(@NonNull DataContainer dataContainer) {
        DataContainer container = dataContainers.remove(dataContainer.getIdAsString());
        if (container != null) connection.deleteRow(this, dataContainer.getIdAsString());
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
        return dataContainers.get(id);
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
    public boolean contains(@NonNull String id) {
        return dataContainers.containsKey(id);
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
    public List<DataContainer> getDataContainers() {
        return dataContainers.values().stream().toList();
    }
}