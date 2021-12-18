package mrnavastar.sqlib.api;

import com.google.common.primitives.Ints;
import mrnavastar.sqlib.util.SqlManager;
import mrnavastar.sqlib.api.databases.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Table {

    private final String name;
    private final ArrayList<DataContainer> dataContainers = new ArrayList<>();
    private boolean isInTransaction = false;
    private Database database;

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addToDatabase(Database database) {
        this.database = database;
        this.beginTransaction();
        SqlManager.createTable(name, database.getType());

        List<String> ids = this.getIds();
        if (ids != null) {
            for (String id : ids) {
                DataContainer dataContainer = new DataContainer(id);
                dataContainers.add(dataContainer);
                dataContainer.link(this, this.database);
            }
        }
        this.endTransaction();
    }

    public Database getDatabase() {
        return this.database;
    }

    public void beginTransaction() {
        if (!this.isInTransaction) {
            database.connect();
            database.beginTransaction();
            this.isInTransaction = true;
        }
    }

    public void endTransaction() {
        database.endTransaction();
        database.disconnect();
        this.isInTransaction = false;
    }

    public boolean isInTransaction() {
        return isInTransaction;
    }

    public List<String> getIds() {
        if (!this.isInTransaction) this.database.connect();
        List<String> ids = SqlManager.listIds(this.name);
        if (!this.isInTransaction) this.database.disconnect();
        return ids;
    }

    public List<UUID> getIdsAsUuids() {
        List<UUID> uuids = new ArrayList<>();
        for (String id : getIds()) uuids.add(UUID.fromString(id));
        return uuids;
    }

    public int[] getIdsAsInts() {
        List<Integer> ints = new ArrayList<>();
        for (String id : getIds()) ints.add(Integer.parseInt(id));
        return Ints.toArray(ints);
    }

    public DataContainer createDataContainer(String id) {
        DataContainer dataContainer = new DataContainer(id);
        this.put(dataContainer);
        return dataContainer;
    }

    public DataContainer createDataContainer(UUID id) {
       return createDataContainer(id.toString());
    }

    public DataContainer createDataContainer(int id) {
       return createDataContainer(String.valueOf(id));
    }

    public void put(DataContainer dataContainer) {
        if (!this.isInTransaction) this.database.connect();
        if (this.get(dataContainer.getId()) != null) this.drop(dataContainer);
        SqlManager.createRow(this.name, dataContainer.getId());
        this.dataContainers.add(dataContainer);
        dataContainer.link(this, this.database);
        if (!this.isInTransaction) this.database.disconnect();
    }

    public void drop(DataContainer dataContainer) {
        if (!this.isInTransaction) this.database.connect();
        SqlManager.deleteRow(this.getName(), dataContainer.getId());
        dataContainer.link(null, null);
        this.dataContainers.remove(dataContainer);
        if (!this.isInTransaction) this.database.disconnect();
    }

    public void drop(String id) {
        drop(this.get(id));
    }

    public void drop(UUID uuid) {
        drop(uuid.toString());
    }

    public void drop(int i) {
        drop(String.valueOf(i));
    }

    public DataContainer get(String id) {
        for (DataContainer dataContainer : this.dataContainers) {
            if (dataContainer.getId().equals(id)) return dataContainer;
        }
        return null;
    }

    public DataContainer get(UUID id) {
        return get(id.toString());
    }

    public DataContainer get(int id) {
        return get(String.valueOf(id));
    }

    public ArrayList<DataContainer> getDataContainers() {
        return this.dataContainers;
    }

    public boolean contains(String id) {
        for (DataContainer dataContainer : this.dataContainers) {
            if (dataContainer.getId().equals(id)) return true;
        }
        return false;
    }

    public boolean contains(UUID id) {
        return contains(id.toString());
    }

    public boolean contains(int id) {
        return contains(String.valueOf(id));
    }
}