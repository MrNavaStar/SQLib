package mrnavastar.sqlib;

import com.google.gson.JsonObject;
import mrnavastar.sqlib.api.DataContainer;
import mrnavastar.sqlib.api.Table;
import mrnavastar.sqlib.util.Database;
import net.fabricmc.api.ModInitializer;
import net.minecraft.nbt.NbtCompound;

public class Sqlib implements ModInitializer {
    @Override
    public void onInitialize() {
        Database.TYPE = "SQLITE";
        Database.DATABASE_NAME = "MyAmazingDatabase";
        Database.SQLITE_DIRECTORY = "/home/ethan/test";

        Database.init();

        Table asd = new Table("sdad");
        DataContainer test = new DataContainer("sdad");
        asd.put(test);

        test.put("S", "sdad");
        test.put("S", 1);
        test.put("S", true);
        test.put("S", new JsonObject());
        NbtCompound s = new NbtCompound();
        s.putInt("sda", 1);
        test.put("S", s);
    }
}