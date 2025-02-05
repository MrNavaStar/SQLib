package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.api.database.Database;
import me.mrnavastar.sqlib.impl.config.Config;

import java.util.List;

public class SQLib {

    protected static Database database;

    public static Database getDatabase() {
        Config.load();
        return database;
    }

    public static List<Database> getDatabases() {
        return Database.getDatabases();
    }
}