package mrnavastar.sqlib;

import mrnavastar.sqlib.api.DataContainer;
import mrnavastar.sqlib.api.Table;
import mrnavastar.sqlib.util.Database;
import net.fabricmc.api.ModInitializer;

public class Sqlib implements ModInitializer {
    @Override
    public void onInitialize() {
        Database.TYPE = "SQLITE";
        Database.DATABASE_NAME = "POGGERS";
        Database.SQLITE_DIRECTORY = "/home/ethan/test";

        Database.init();

        Table table = new Table("Ssdsad");
        DataContainer data = new DataContainer("123");
        table.put(data);
        
        System.out.println(table.get("123").getId());
    }
}