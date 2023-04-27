package mrnavastar.sqlib;

import mrnavastar.sqlib.sql.SQLConnection;
import mrnavastar.sqlib.database.Database;
import mrnavastar.sqlib.sql.SQLDataType;

import java.util.*;

public class Table {

    private final String name;
    private final HashMap<String, SQLDataType> columns = new HashMap<>();
    private String primaryKey = "";
    private SQLDataType primaryKeyType;
    private final HashMap<String, DataContainer> dataContainers = new HashMap<>();
    private boolean isInTransaction = false;
    private final Database database;
    private final SQLConnection sqlConnection;

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
        primaryKeyType = columns.remove(primaryKey);
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
        return primaryKeyType;
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

    /*public List<UUID> getIdsAsUuids() {
        List<UUID> uuids = new ArrayList<>();
        for (String id : getIds()) uuids.add(UUID.fromString(id));
        return uuids;
    }

    public int[] getIdsAsInts() {
        List<Integer> ints = new ArrayList<>();
        for (String id : getIds()) ints.add(Integer.parseInt(id));
        return Ints.toArray(ints);
    }*/

    public DataContainer createDataContainer(String id) {
        DataContainer dataContainer = get(id);
        if (dataContainer != null) this.drop(dataContainer);

        dataContainer = new DataContainer(id, this, sqlConnection);
        sqlConnection.createRow(this, dataContainer.getId());
        dataContainers.put(dataContainer.getId(), dataContainer);

        return dataContainer;
    }

    /*public DataContainer createDataContainer(UUID id) {
       return createDataContainer(id.toString());
    }

    public DataContainer createDataContainer(int id) {
       return createDataContainer(String.valueOf(id));
    }*/

    public void drop(DataContainer dataContainer) {
        DataContainer container = dataContainers.remove(dataContainer.getId());
        if (container != null) sqlConnection.deleteRow(this, dataContainer.getId());
    }

    public void drop(String id) {
        drop(get(id));
    }

    /*public void drop(UUID uuid) {
        drop(uuid.toString());
    }

    public void drop(int i) {
        drop(String.valueOf(i));
    }*/

    public DataContainer get(String id) {
        return dataContainers.get(id);
    }

/*    public DataContainer get(UUID id) {
        return get(id.toString());
    }

    public DataContainer get(int id) {
        return get(String.valueOf(id));
    }*/

    public Collection<DataContainer> getDataContainers() {
        return dataContainers.values();
    }

    public boolean contains(String id) {
        return dataContainers.containsKey(id);
    }

    /*public boolean contains(UUID id) {
        return contains(id.toString());
    }

    public boolean contains(int id) {
        return contains(String.valueOf(id));
    }*/
}