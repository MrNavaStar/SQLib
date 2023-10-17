package mrnavastar.sqlib;

import lombok.Getter;
import mrnavastar.sqlib.sql.SQLConnection;
import mrnavastar.sqlib.database.Database;
import mrnavastar.sqlib.sql.SQLDataType;

import java.io.IOException;
import java.util.*;

public class Table {

    @Getter
    private final String name;
    @Getter
    private final String modId;
    @Getter
    private final Database database;
    private final SQLConnection sqlConnection;

    @Getter
    private final HashMap<String, SQLDataType> columns = new HashMap<>();
    private final HashMap<String, DataContainer> dataContainers = new HashMap<>();

    private boolean isInTransaction = false;
    protected boolean autoIncrement = false;

    public Table(String modId, String name, Database database, SQLConnection sqlConnection) {
        this.name = name;
        this.modId = modId;
        this.database = database;
        this.sqlConnection = sqlConnection;
    }

    public Table setAutoIncrement() {
        autoIncrement = true;
        return this;
    }

    public Table addColumn(String name, SQLDataType dataType) {
        columns.put(name, dataType);
        return this;
    }

    public Table finish() {
        sqlConnection.createTable(this, autoIncrement);
        database.addTable(this);
        sqlConnection.listPrimaryKeys(this).forEach(key -> dataContainers.put(key, new DataContainer(key, this, sqlConnection)));
        return this;
    }

    public String getNoConflictName() {
        return modId + "_" + name;
    }

    public void beginTransaction() {
        if (!isInTransaction) {
            database.beginTransaction();
            isInTransaction = true;
        }
    }

    public void endTransaction() {
        database.endTransaction();
        isInTransaction = false;
    }

    public boolean isInTransaction() {
        return isInTransaction;
    }

    public ArrayList<String> getIds() {
        return (ArrayList<String>) dataContainers.keySet().stream().toList();
    }

    public ArrayList<UUID> getIdsAsUUIDs() {
        ArrayList<UUID> uuids = new ArrayList<>();
        dataContainers.keySet().forEach(id -> uuids.add(UUID.fromString(id)));
        return uuids;
    }

    public ArrayList<Integer> getIdsAsInts() {
        ArrayList<Integer> ints = new ArrayList<>();
        dataContainers.keySet().forEach(id -> ints.add(Integer.parseInt(id)));
        return ints;
    }

    public DataContainer createDataContainer(String id) {
        if (!autoIncrement) {
            DataContainer dataContainer = get(id);
            if (dataContainer != null) this.drop(dataContainer);
        }

        int autoId = sqlConnection.createRow(this, id, autoIncrement);
        if (autoIncrement) id = String.valueOf(autoId);

        DataContainer dataContainer = new DataContainer(id, this, sqlConnection);
        dataContainers.put(id, dataContainer);
        return dataContainer;
    }

    public DataContainer createDataContainer(UUID id) {
       return createDataContainer(id.toString());
    }

    public DataContainer createDataContainer(int id) {
       return createDataContainer(String.valueOf(id));
    }

    public DataContainer getOrCreateDataContainer(String id) {
        DataContainer dataContainer = get(id);
        if (dataContainer == null) dataContainer = createDataContainer(id);
        return dataContainer;
    }

    public DataContainer getOrCreateDataContainer(UUID id) {
        return getOrCreateDataContainer(id.toString());
    }

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

    public void drop(DataContainer dataContainer) {
        DataContainer container = dataContainers.remove(dataContainer.getIdAsString());
        if (container != null) sqlConnection.deleteRow(this, dataContainer.getIdAsString());
    }

    public void drop(String id) {
        drop(get(id));
    }

    public void drop(UUID id) {
        drop(id.toString());
    }

    public void drop(int id) {
        drop(String.valueOf(id));
    }

    public DataContainer get(String id) {
        return dataContainers.get(id);
    }

    public DataContainer get(UUID id) {
        return get(id.toString());
    }

    public DataContainer get(int id) {
        return get(String.valueOf(id));
    }

    public boolean contains(String id) {
        return dataContainers.containsKey(id);
    }

    public boolean contains(UUID id) {
        return contains(id.toString());
    }

    public boolean contains(int id) {
        return contains(String.valueOf(id));
    }

    public ArrayList<DataContainer> getDataContainers() {
        return (ArrayList<DataContainer>) dataContainers.values().stream().toList();
    }
}