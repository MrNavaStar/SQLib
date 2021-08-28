package mrnavastar.sqlib.api;

import com.google.gson.JsonObject;
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
                JsonObject obj = new JsonObject();
                obj.addProperty("ID", id);
                obj.add("STRINGS", SqlManager.readJson(this.name, id, "STRINGS"));
                obj.add("INTS", SqlManager.readJson(this.name, id, "INTS"));
                obj.add("BOOLEANS", SqlManager.readJson(this.name, id, "BOOLEANS"));
                obj.add("JSON", SqlManager.readJson(this.name, id, "JSON"));

                obj.add("NBT", SqlManager.readJson(this.name, id, "NBT"));

                DataContainer dataContainer = new DataContainer(obj);
                dataContainers.add(dataContainer);
            }
        }
        Database.disconnect();
    }

    public String getName() {
        return name;
    }

    public ArrayList<DataContainer> getDataContainers() {
        return dataContainers;
    }

    public void put(DataContainer dataContainer) {
        dataContainers.add(dataContainer);
    }

    public void save(){
        Database.connect();
        Database.saveTable(this);
        Database.disconnect();
    }

    public void saveDataContainer(DataContainer dataContainer) {
        Database.connect();
        Database.saveDataContainer(this, dataContainer);
        Database.disconnect();
    }
}