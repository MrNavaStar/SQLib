package mrnavastar.sqlib;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mrnavastar.sqlib.database.SQLiteDatabase;
import mrnavastar.sqlib.sql.SQLDataType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;
import java.util.UUID;

public class SQLib {

    public static final String MOD_ID = "SQLib";
    public static final Gson GSON = new Gson();

    public static void log(Level level, String message) {
        LogManager.getLogger().log(level, "[" + MOD_ID + "] " + message);
    }

    public static void main(String[] args) {
        SQLiteDatabase d = new SQLiteDatabase("Test", "run");
        Table t = d.createTable("Test")
                .addColumn("S", SQLDataType.STRING)
                .addColumn("I", SQLDataType.INT)
                .addColumn("F", SQLDataType.FLOAT)
                .addColumn("D", SQLDataType.DOUBLE)
                .addColumn("L", SQLDataType.LONG)
                .addColumn("B", SQLDataType.BOOL)
                .addColumn("DA", SQLDataType.DATE)
                .addColumn("T", SQLDataType.TIME)
                .addColumn("TS", SQLDataType.TIMESTAMP)
                .addColumn("Y", SQLDataType.YEAR)
                .addColumn("U", SQLDataType.UUID)
                .addColumn("BP", SQLDataType.BLOCKPOS)
                .addColumn("CP", SQLDataType.CHUNKPOS)
                .addColumn("J", SQLDataType.JSON)
                .addColumn("N", SQLDataType.NBT)
                .addColumn("M", SQLDataType.MUTABLE_TEXT)
                .finish();

        DataContainer dc = t.createDataContainer(UUID.randomUUID());
        dc.put("S", "TEST");
        dc.put("I", 123);
        dc.put("F", 123.321F);
        dc.put("D", 123.321);
        dc.put("L", 1234567890);
        dc.put("B", false);
        dc.put("DA", new Date(2005, 4, 19));
        dc.put("T", new Time(3, 5, 2));
        dc.put("TS", Timestamp.from(Instant.now()));
        dc.put("Y", Year.now());
        dc.put("U", UUID.randomUUID());
        dc.put("BP", new BlockPos(1, 2, 3));
        dc.put("CP", new ChunkPos(1, 2));
        dc.put("J", new JsonObject());
        dc.put("N", new NbtCompound());
        dc.put("M", (MutableText) Text.of("test"));

        System.out.println(dc.getString("S"));
        System.out.println(dc.getInt("I"));
        System.out.println(dc.getFloat("F"));
        System.out.println(dc.getDouble("D"));
        System.out.println(dc.getLong("L"));
        System.out.println(dc.getBool("B"));
        System.out.println(dc.getDate("DA"));
        System.out.println(dc.getTime("T"));
        System.out.println(dc.getTimestamp("TS"));
        System.out.println(dc.getYear("Y"));
        System.out.println(dc.getUUID("U"));
        System.out.println(dc.getBlockPos("BP"));
        System.out.println(dc.getChunkPos("CP"));
        System.out.println(dc.getJson("J"));
        System.out.println(dc.getNbt("N"));
        System.out.println(dc.getMutableText("M"));
    }
}