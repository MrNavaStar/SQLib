package me.mrnavastar.sqlib;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.api.DataContainer;
import me.mrnavastar.sqlib.api.Table;
import me.mrnavastar.sqlib.sql.ColumnType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestMod implements ModInitializer {

    private final String testString = "Test String";
    private final byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);

    private final Date testDate = new Date();
    private final Color testColor = Color.CYAN;
    private final UUID testUuid = UUID.randomUUID();

    private final BlockPos testBlockpos = new BlockPos(7, 8, 9);
    private final ChunkPos testChunkpos = new ChunkPos(5, 99);
    private final JsonObject testJson = new JsonObject();
    private final NbtCompound testNbt = new NbtCompound();
    private final MutableText testText = (MutableText) Text.of("Test Text");
    private final Identifier testIdentifier = Identifier.tryParse("test:identifier");

    private final Key testKey = Key.key("test:key");
    private final Component testComponent = Component.text("Test Component");

    private void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual) || !actual.getClass().equals(expected.getClass())) throw new RuntimeException("Expected " + expected.getClass().getName() + " but got " + actual.getClass().getName());
    }

    private void assertNotEquals(Object o1, Object o2) {
        if (o1.equals(o2)) throw new RuntimeException("Expected objects to be equal, but they are the same");
    }

    private void assertTrue(boolean bool) {
        if (!bool) throw new RuntimeException("Expected True but got False");
    }

    private void assertFalse(boolean bool) {
        if (bool) throw new RuntimeException("Expected False but got True");
    }

    private void testAllTransactions() {
        Table table = SQLib.getDatabase().createTable("test", "table1")
                .column("byte", ColumnType.BYTE)
                .column("bytes", ColumnType.BYTES)
                .column("bool", ColumnType.BOOL)
                .column("short", ColumnType.SHORT)
                .column("int", ColumnType.INT)
                .column("float", ColumnType.FLOAT)
                .column("double", ColumnType.DOUBLE)
                .column("long", ColumnType.LONG)
                .column("string", ColumnType.STRING)
                .column("char", ColumnType.CHAR)

                .column("date", ColumnType.DATE)
                .finish();

        DataContainer container = table.createDataContainer();

        // Test Byte
        container.put(ColumnType.BYTE, "byte", Byte.MAX_VALUE);
        assertEquals(Byte.MAX_VALUE, container.get(ColumnType.BYTE, "byte"));
        container.put(ColumnType.BYTE, "byte", Byte.MIN_VALUE);
        assertEquals(Byte.MIN_VALUE, container.get(ColumnType.BYTE, "byte"));
        container.put(ColumnType.BYTE, "byte", (byte) 0);
        assertEquals((byte) 0, container.get(ColumnType.BYTE, "byte"));

        // Test Bytes
        /*container.put(ColumnType.BYTES, "bytes", testBytes);
        assertEquals(testBytes, container.get(ColumnType.BYTES, "bytes"));*/

        // Test Bool
        container.put(ColumnType.BOOL, "bool", true);
        assertTrue(container.get(ColumnType.BOOL, "bool"));
        container.put(ColumnType.BOOL, "bool", false);
        assertFalse(container.get(ColumnType.BOOL, "bool"));

        // Test Short
        container.put(ColumnType.SHORT, "short", Short.MAX_VALUE);
        assertEquals(Short.MAX_VALUE, container.get(ColumnType.SHORT, "short"));
        container.put(ColumnType.SHORT, "short", Short.MIN_VALUE);
        assertEquals(Short.MIN_VALUE, container.get(ColumnType.SHORT, "short"));
        container.put(ColumnType.SHORT, "short", (short) 0);
        assertEquals((short) 0, container.get(ColumnType.SHORT, "short"));

        // Test Int
        container.put(ColumnType.INT, "int", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, container.get(ColumnType.INT, "int"));
        container.put(ColumnType.INT, "int", Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, container.get(ColumnType.INT, "int"));
        container.put(ColumnType.INT, "int", 0);
        assertEquals(0, container.get(ColumnType.INT, "int"));

        // Test Float
        container.put(ColumnType.FLOAT, "float", Float.MAX_VALUE);
        assertEquals(Float.MAX_VALUE, container.get(ColumnType.FLOAT, "float"));
        container.put(ColumnType.FLOAT, "float", Float.MIN_VALUE);
        assertEquals(Float.MIN_VALUE, container.get(ColumnType.FLOAT, "float"));
        container.put(ColumnType.FLOAT, "float", 0F);
        assertEquals(0F, container.get(ColumnType.FLOAT, "float"));

        // Test Double
        container.put(ColumnType.DOUBLE, "double", Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, container.get(ColumnType.DOUBLE, "double"));
        container.put(ColumnType.DOUBLE, "double", Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, container.get(ColumnType.DOUBLE, "double"));
        container.put(ColumnType.DOUBLE, "double", 0.0);
        assertEquals(0.0, container.get(ColumnType.DOUBLE, "double"));

        // Test Long
        container.put(ColumnType.LONG, "long", Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, container.get(ColumnType.LONG, "long"));
        container.put(ColumnType.LONG, "long", Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, container.get(ColumnType.LONG, "long"));
        container.put(ColumnType.LONG, "long", 0L);
        assertEquals(0L, container.get(ColumnType.LONG, "long"));

        // Test String
        container.put(ColumnType.STRING, "string", "test");
        assertEquals("test",  container.get(ColumnType.STRING, "string"));

        // Test Char
        container.put(ColumnType.CHAR, "char", 'c');
        assertEquals('c',  container.get(ColumnType.CHAR, "char"));

        // Test Date
        Date date = new Date();
        container.put(ColumnType.DATE, "date", date);
        assertEquals(date, container.get(ColumnType.DATE, "date"));
    }

    private void testTableFunctions() {
        Table table = SQLib.getDatabase().createTable("test", "table2")
                .column("test", ColumnType.STRING)
                .finish();

        assertEquals("test_table2", table.getNoConflictName());
        assertNotEquals(null, SQLib.getDatabase().getTable("test", "table2"));
        assertNotEquals(0, SQLib.getDatabase().getTables().size());


        //assertEquals(0, table.getDataContainers().size());
    }

    @Override
    @SneakyThrows
    public void onInitialize() {
        System.out.println("---------- Starting Tests ----------");
        System.out.println("Starting Transactions");
        testAllTransactions();
        //System.out.println("Starting Table Functions");
        //testTableFunctions();
        System.out.println("--------------- Done ----------------");

        SQLib.getDatabase().close();
        new File(FabricLoader.getInstance().getGameDir() + "/sqlib").delete();
    }
}