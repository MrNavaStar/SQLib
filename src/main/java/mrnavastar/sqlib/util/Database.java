package mrnavastar.sqlib.util;

import mrnavastar.sqlib.api.DataContainer;
import mrnavastar.sqlib.api.Table;

import java.util.ArrayList;

public class Database {

    public static String TYPE;
    //General
    public static String DATABASE_NAME;
    //SQLITE
    public static String SQLITE_PATH;
    //MYSQL
    public static String MYSQL_ADDRESS;
    public static String MYSQL_PORT;
    public static String MYSQL_USERNAME;
    public static String MYSQL_PASSWORD;

    private static final ArrayList<Table> tables = new ArrayList<>();

    public static void connect() {
        switch (TYPE) {
            case "SQLITE" -> SqlManager.connectSQLITE(SQLITE_PATH, DATABASE_NAME);
            case "MYSQL" -> SqlManager.connectMYSQL(MYSQL_ADDRESS, MYSQL_PORT, DATABASE_NAME, MYSQL_USERNAME, MYSQL_PASSWORD);
        }
    }

    public static void disconnect() {
        SqlManager.disconnect();
    }

    public static void init() {
        connect();
        disconnect();
    }

    public static void addTable(Table table) {
        if (!tables.contains(table)) tables.add(table);
    }

    public static void saveAll() {
        for (Table table : tables) {
            saveTable(table);
        }
    }

    public static void saveTable(Table table) {
        String tableName = table.getName();

        for (DataContainer dataContainer : table.getDataContainers()) {
            String id = dataContainer.getId();
            SqlManager.createRow(tableName, id);
            SqlManager.writeJson(tableName, id, "STRINGS", dataContainer.getStrings());
            SqlManager.writeJson(tableName, id, "INTS", dataContainer.getInts());
            SqlManager.writeJson(tableName, id, "BOOLEANS", dataContainer.getBooleans());
            SqlManager.writeJson(tableName, id, "JSON", dataContainer.getJsonObjects());
            SqlManager.writeJson(tableName, id, "NBT", dataContainer.getNbts());
        }
    }

    public static void saveDataContainer(Table table, DataContainer dataContainer) {
        String tableName = table.getName();
        String id = dataContainer.getId();
        SqlManager.createRow(table.getName(), dataContainer.getId());
        SqlManager.writeJson(tableName, id, "STRINGS", dataContainer.getStrings());
        SqlManager.writeJson(tableName, id, "INTS", dataContainer.getInts());
        SqlManager.writeJson(tableName, id, "BOOLEANS", dataContainer.getBooleans());
        SqlManager.writeJson(tableName, id, "JSON", dataContainer.getJsonObjects());
        SqlManager.writeJson(tableName, id, "NBT", dataContainer.getNbts());
    }
}