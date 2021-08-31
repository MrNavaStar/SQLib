package mrnavastar.sqlib.util;

import mrnavastar.sqlib.api.SqlTypes;
import mrnavastar.sqlib.api.Table;

import java.util.ArrayList;

public class Database {

    public static Enum<SqlTypes> TYPE;
    //General
    public static String DATABASE_NAME;
    //SQLITE
    public static String SQLITE_DIRECTORY;
    //MYSQL
    public static String MYSQL_ADDRESS;
    public static String MYSQL_PORT;
    public static String MYSQL_USERNAME;
    public static String MYSQL_PASSWORD;

    private static final ArrayList<Table> tables = new ArrayList<>();

    public static void connect() {
        if (TYPE.equals(SqlTypes.SQLITE)) SqlManager.connectSQLITE(SQLITE_DIRECTORY, DATABASE_NAME);
        if (TYPE.equals(SqlTypes.MYSQL)) SqlManager.connectMYSQL(MYSQL_ADDRESS, MYSQL_PORT, DATABASE_NAME, MYSQL_USERNAME, MYSQL_PASSWORD);
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
}