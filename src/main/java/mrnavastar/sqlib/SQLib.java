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

public class SQLib {

    public static final String MOD_ID = "SQLib";
    public static final Gson GSON = new Gson();

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }
}