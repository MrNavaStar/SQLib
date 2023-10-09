package mrnavastar.sqlib;

import com.google.gson.Gson;
import mrnavastar.sqlib.database.Database;
import mrnavastar.sqlib.database.SQLiteDatabase;
import mrnavastar.sqlib.sql.SQLDataType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class SQLib implements ModInitializer {

    public static final String MOD_ID = "SQLib";
    public static final Gson GSON = new Gson();

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }

    @Override
    public void onInitialize() {
        Database d = new SQLiteDatabase(MOD_ID, "test", "config");
        Table t = d.createTable("poggers").addColumn("t", SQLDataType.MUTABLE_TEXT).finish();

        MutableText pain = (MutableText) Text.of("pof");
        pain.formatted(Formatting.RED);

        DataContainer testy = t.createDataContainer("poggggg");
        testy.put("t", pain);

        MutableText ppp = testy.getMutableText("t");
        System.out.println(ppp.toString());
    }
}