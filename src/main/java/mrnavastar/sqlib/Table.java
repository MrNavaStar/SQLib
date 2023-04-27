package mrnavastar.sqlib;

import mrnavastar.sqlib.sql.SQLConnection;
import mrnavastar.sqlib.database.Database;
import mrnavastar.sqlib.sql.SQLDataType;

import java.util.*;

public class Table {

    private final String name;
    private final Database database;
    private final SQLConnection sqlConnection;
    private String primaryKey = "";

    private final HashMap<String, SQLDataType> columns = new HashMap<>();
    private final HashMap<String, DataContainer> dataContainers = new HashMap<>();

    private boolean isInTransaction = false;


    public Table(String name, Database database) {
        this.name = name;
        this.database = database;
        this.sqlConnection = database.getSqlManager();
    }

    public Table addColumn(String name, SQLDataType dataType) {
        columns.put(name, dataType);
        return this;
    }

    public Table setContainerId(String name) {
        primaryKey = name;
        return this;
    }

    public Table finish() {
        columns.remove(primaryKey);
        database.addTable(this);

        getIds().forEach(this::createDataContainer);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Database getDatabase() {
        return database;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public SQLDataType getPrimaryKeyType() {
        return SQLDataType.STRING;
    }

    public HashMap<String, SQLDataType> getColumns() {
        return columns;
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

    public List<String> getIds() {
        return sqlConnection.listPrimaryKeys(this);
    }

    public List<UUID> getIdsAsUuids() {
        List<UUID> uuids = new ArrayList<>();
        dataContainers.keySet().forEach(id -> uuids.add(UUID.fromString(id)));
        return uuids;
    }

    public DataContainer createDataContainer(String id) {
        DataContainer dataContainer = get(id);
        if (dataContainer != null) this.drop(dataContainer);

        dataContainer = new DataContainer(id, this, sqlConnection);
        sqlConnection.createRow(this, id);
        dataContainers.put(id, dataContainer);

        return dataContainer;
    }

    public DataContainer createDataContainer(UUID id) {
       return createDataContainer(id.toString());
    }

    public void drop(DataContainer dataContainer) {
        DataContainer container = dataContainers.remove(dataContainer.getIdAsString());
        if (container != null) sqlConnection.deleteRow(this, dataContainer.getIdAsString());
    }

    public void drop(String id) {
        drop(get(id));
    }

    public void drop(UUID uuid) {
        drop(uuid.toString());
    }

    public DataContainer get(String id) {
        return dataContainers.get(id);
    }

   public DataContainer get(UUID id) {
        return get(id.toString());
    }

    public boolean contains(String id) {
        return dataContainers.containsKey(id);
    }

    public boolean contains(UUID id) {
        return contains(id.toString());
    }

    public Collection<DataContainer> getDataContainers() {
        return dataContainers.values();
    }
}