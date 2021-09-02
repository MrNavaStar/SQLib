package mrnavastar.sqlib.api;

import mrnavastar.sqlib.util.Database;
import mrnavastar.sqlib.util.SqlManager;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final String name;
    private final ArrayList<DataContainer> dataContainers = new ArrayList<>();
    private boolean inTransaction = false;

    public Table(String name) {
        this.name = name;
        this.beginTransaction();
        SqlManager.createTable(name);
        Database.addTable(this);

        List<String> ids = this.getIds();
        if (ids != null) {
            for (String id : ids) {
                DataContainer dataContainer = new DataContainer(id);
                dataContainers.add(dataContainer);
                dataContainer.setTable(this);
            }
        }
        this.endTransaction();
    }

    public String getName() {
        return this.name;
    }

    public void beginTransaction() {
        Database.connect();
        SqlManager.beginTransaction();
        this.inTransaction = true;
    }

    public void endTransaction() {
        SqlManager.endTransaction();
        Database.disconnect();
        this.inTransaction = false;
    }

    public boolean isInTransaction() {
        return inTransaction;
    }

    public List<String> getIds() {
        if (!this.inTransaction) Database.connect();
        List<String> ids = SqlManager.listIds(this.name);
        if (!this.inTransaction) Database.disconnect();
        return ids;
    }

    public void put(DataContainer dataContainer) {
        if (this.get(dataContainer.getId()) == null) {
            if (!this.inTransaction) Database.connect();
            SqlManager.createRow(this.name, dataContainer.getId());
            if (!this.inTransaction) Database.disconnect();
            dataContainers.add(dataContainer);
            dataContainer.setTable(this);
        }
    }

    public void drop(String id) {
        DataContainer dataContainer = this.get(id);
        if (dataContainer != null) {
            dataContainer.setTable(null);
            dataContainers.remove(dataContainer);
        }
    }

    public DataContainer get(String id) {
        for (DataContainer dataContainer : this.dataContainers) {
            if (dataContainer.getId().equals(id)) return dataContainer;
        }
        return null;
    }
}