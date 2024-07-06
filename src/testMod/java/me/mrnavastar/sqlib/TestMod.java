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

    private final Key testKey = Key.key("test:key");
    private final Component testComponent = Component.text("Test Component");

    private void assertEquals(Object expected, Object actual) {
        if (expected instanceof byte[]) {
            if (expected != actual) throw new RuntimeException("Expected " + expected.getClass().getName() + " but got " + actual.getClass().getName());
        }
        else if (!expected.equals(actual)) throw new RuntimeException("Expected " + expected.getClass().getName() + " but got " + actual.getClass().getName());
        System.out.println("Poggers");
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

    private void testAllTransactions() throws SQLException {
        Table table = SQLib.getDatabase().createTable("test", "table1")
                .column("byte", ColumnType.BYTE)
                .column("bytes", ColumnType.BYTES)
                .column("bool", ColumnType.BOOL)
                .column("short", ColumnType.SHORT)
                .finish();

        DataContainer container = table.createDataContainer();
        System.out.println(container.getId());

        // Test Byte
        container.put(ColumnType.BYTE, "byte", Byte.MAX_VALUE);
        assertEquals(Byte.MAX_VALUE, container.get(ColumnType.BYTE, "byte"));
        container.put(ColumnType.BYTE, "byte", Byte.MIN_VALUE);
        assertEquals(Byte.MIN_VALUE, container.get(ColumnType.BYTE, "byte"));
        //container.put(ColumnType.BYTE, "byte", (byte) 0);
        //assertEquals((byte) 0, container.get(ColumnType.BYTE, "byte"));

        // Test Bytes
        /*container.put(ColumnType.BYTES, "bytes", testBytes);
        assertEquals(testBytes, container.get(ColumnType.BYTES, "bytes"));*/

        // Test Bool
        container.put(ColumnType.BOOL, "bool", true);
        assertTrue(container.get(ColumnType.BOOL, "bool"));
        container.put(ColumnType.BOOL, "bool", false);
        assertFalse(container.get(ColumnType.BOOL, "bool"));

        //Test Short
        /*container.put(ColumnType.SHORT, "short", Short.MAX_VALUE);
        assertEquals(Short.MAX_VALUE, container.get(ColumnType.SHORT, "short"));
        container.put(ColumnType.SHORT, "short", Short.MIN_VALUE);
        assertEquals(Short.MIN_VALUE, container.get(ColumnType.SHORT, "short"));
        container.put(ColumnType.SHORT, "short", (short) 0);
        assertEquals((short) 0, container.get(ColumnType.SHORT, "short"));*/
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