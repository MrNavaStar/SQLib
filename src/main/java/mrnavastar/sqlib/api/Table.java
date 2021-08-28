package mrnavastar.sqlib.api;

import mrnavastar.sqlib.util.Database;
import mrnavastar.sqlib.util.SqlManager;

import java.util.ArrayList;

public class Table {

    private final String name;
    private final ArrayList<DataContainer> dataContainers = new ArrayList<>();

    public Table(String name) {
        this.name = name;
        Database.connect();
        SqlManager.createTable(name);
        Database.addTable(this);

        ArrayList<String> ids = SqlManager.listIds(this.name);
        if (ids != null) {
            for (String id : ids) {
                DataContainer dataContainer = new DataContainer(id);
                dataContainers.add(dataContainer);
                dataContainer.setTableName(this.name);
            }
        }
        Database.disconnect();
    }

    public void put(DataContainer dataContainer) {
        dataContainers.add(dataContainer);
        dataContainer.setTableName(this.name);
    }

    public DataContainer get(String id) {
        for (DataContainer dataContainer : this.dataContainers) {
            if (dataContainer.getId().equals(id)) return dataContainer;
        }
        return null;
    }
}