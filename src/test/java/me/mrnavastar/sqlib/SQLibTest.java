package me.mrnavastar.sqlib;

import com.google.gson.JsonObject;
import me.mrnavastar.sqlib.database.Database;
import me.mrnavastar.sqlib.sql.SQLDataType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SQLibTest {

    private static Database database;

    private final String testString = "Test String";
    private final int testInt = 37;
    private final double testDouble = Math.PI;
    private final long testLong = 3458309862334564524L;
    private final boolean testBool = true;
    private final Date testDate = new Date();
    private final Color testColor = Color.CYAN;
    private final UUID testUuid = UUID.randomUUID();
    private final BlockPos testBlockpos = new BlockPos(7, 8, 9);
    private final ChunkPos testChunkpos = new ChunkPos(5, 99);
    private final JsonObject testJson = new JsonObject();
    private final NbtCompound testNbt = new NbtCompound();
    private final MutableText testText = (MutableText) Text.of("Test Text");
    private final Identifier testIdentifier = Identifier.tryParse("test:identifier");


    @BeforeAll
    public static void init() {
        SQLib.init(Path.of("."), Path.of("."));
        database = SQLib.getDatabase();
    }

    @Test
    public void testAllTransactions() {
        Table table = database.createTable("test", "table1")
                .addColumn("string", SQLDataType.STRING)
                .addColumn("int", SQLDataType.INT)
                .addColumn("double", SQLDataType.DOUBLE)
                .addColumn("long", SQLDataType.LONG)
                .addColumn("bool", SQLDataType.BOOL)
                .addColumn("date", SQLDataType.DATE)
                .addColumn("uuid", SQLDataType.UUID)
                .addColumn("blockpos", SQLDataType.BLOCKPOS)
                .addColumn("chunkpos", SQLDataType.CHUNKPOS)
                .addColumn("json", SQLDataType.JSON)
                .addColumn("nbt", SQLDataType.NBT)
                .addColumn("text", SQLDataType.MUTABLE_TEXT)
                .addColumn("identifier", SQLDataType.IDENTIFIER)
                .finish();

        DataContainer dataContainer = table.getOrCreateDataContainer(UUID.randomUUID());
        table.beginTransaction();

        dataContainer.put("string", testString);
        dataContainer.put("int", testInt);
        dataContainer.put("double", testDouble);
        dataContainer.put("long", testLong);
        dataContainer.put("bool", testBool);
        dataContainer.put("date", testDate);
        dataContainer.put("color", testColor);
        dataContainer.put("uuid", testUuid);
        dataContainer.put("blockpos", testBlockpos);
        dataContainer.put("chunkpos", testChunkpos);
        dataContainer.put("json", testJson);
        dataContainer.put("nbt", testNbt);
        //dataContainer.put("text", testText);
        dataContainer.put("identifier", testIdentifier);

        table.endTransaction();

        // Doesn't actually make lookups faster, but here to test if a mod author does this anyway
        table.beginTransaction();

        assertEquals(testString, dataContainer.getString("string"));
        assertEquals(testInt, dataContainer.getInt("int"));
        assertEquals(testDouble, dataContainer.getDouble("double"));
        assertEquals(testLong, dataContainer.getLong("long"));
        assertEquals(testBool, dataContainer.getBool("bool"));
        assertEquals(testDate, dataContainer.getDate("date"));
        assertEquals(testColor, dataContainer.getColor("color"));
        assertEquals(testUuid, dataContainer.getUUID("uuid"));
        assertEquals(testBlockpos, dataContainer.getBlockPos("blockpos"));
        assertEquals(testChunkpos, dataContainer.getChunkPos("chunkpos"));
        assertEquals(testJson, dataContainer.getJson("json"));
        assertEquals(testNbt, dataContainer.getNbt("nbt"));
        //assertEquals(testText, dataContainer.getMutableText("text"));
        assertEquals(testIdentifier, dataContainer.getIdentifier("identifier"));

        dataContainer.clear("string");
        dataContainer.clear("int");
        dataContainer.clear("double");
        dataContainer.clear("long");
        dataContainer.clear("bool");
        dataContainer.clear("date");
        dataContainer.clear("color");
        dataContainer.clear("uuid");
        dataContainer.clear("blockpos");
        dataContainer.clear("chunkpos");
        dataContainer.clear("json");
        dataContainer.clear("nbt");
        dataContainer.clear("text");
        dataContainer.clear("identifier");

        table.endTransaction();
    }

    @Test
    public void testTableFunctions() {
        Table table = database.createTable("test", "table2")
                .addColumn("test", SQLDataType.STRING)
                .finish();

        assertEquals("test_table2", table.getNoConflictName());
        assertNotEquals(null, database.getTable("test", "table2"));
        assertNotEquals(0, database.getTables().size());

        // You can technically mix id types in one table, but certain functions will break if you do so. Not recommended
        // But fine for this test case
        DataContainer dataContainer1 = table.createDataContainer(UUID.randomUUID());
        assertNotEquals(0, table.getIdsAsUUIDs().size());
        assertTrue(table.contains(dataContainer1.getIdAsUUID()));
        table.drop(dataContainer1);
        assertFalse(table.contains(dataContainer1.getIdAsUUID()));

        DataContainer dataContainer2 = table.createDataContainer(1);
        assertNotEquals(0, table.getIdsAsInts().size());
        assertTrue(table.contains(dataContainer2.getIdAsInt()));
        table.drop(dataContainer2);
        assertFalse(table.contains(dataContainer2.getIdAsInt()));

        DataContainer dataContainer3 = table.createDataContainer("test");
        assertNotEquals(0, table.getIds().size());
        assertTrue(table.contains(dataContainer3.getIdAsString()));
        table.drop(dataContainer3);
        assertFalse(table.contains(dataContainer3.getIdAsString()));

        assertEquals(0, table.getDataContainers().size());
    }

    @AfterAll
    public static void cleanup() {
        database.close();
        new File("test.db").delete();
        new File("sqlib.toml").delete();
    }
}