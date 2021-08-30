package mrnavastar.sqlib;

import mrnavastar.sqlib.api.SqlTypes;
import mrnavastar.sqlib.util.Database;
import net.fabricmc.api.ModInitializer;

public class Sqlib implements ModInitializer {
    @Override
    public void onInitialize() {
        Database.TYPE = SqlTypes.SQLITE;
        Database.DATABASE_NAME = "ASDASDSAD";
        Database.SQLITE_DIRECTORY = "/home/ethan/test";
        Database.init();
    }
}