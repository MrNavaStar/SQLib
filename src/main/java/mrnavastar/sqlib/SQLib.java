package mrnavastar.sqlib;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import mrnavastar.sqlib.database.SQLiteDatabase;
import mrnavastar.sqlib.sql.SQLDataType;
import net.minecraft.nbt.NbtCompound;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;

public class SQLib {

    public static final String MOD_ID = "SQLib";
    public static final JsonParser jsonParser = new JsonParser();

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }

    public static void main(String[] args) {
        SQLiteDatabase database = new SQLiteDatabase(MOD_ID, "test", "run");
        Table table = database.createTable("pog").addColumn("bruh", SQLDataType.NBT).finish();
        DataContainer dataContainer = table.createDataContainer(UUID.randomUUID());

        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("poggers", "very cool");

        dataContainer.put("bruh", nbtCompound);

        table.beginTransaction();
        System.out.println(dataContainer.getNbt("bruh").toString());
        table.endTransaction();
    }
}