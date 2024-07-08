package me.mrnavastar.sqlib;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.api.DataContainer;
import me.mrnavastar.sqlib.api.types.AdventureTypes;
import me.mrnavastar.sqlib.api.types.JavaTypes;
import me.mrnavastar.sqlib.api.types.MinecraftTypes;
import me.mrnavastar.sqlib.api.Table;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.io.File;
import java.util.Date;
import java.util.UUID;

public class TestMod implements ModInitializer {

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
                .column("byte", JavaTypes.BYTE)
                .column("bytes", JavaTypes.BYTES)
                .column("bool", JavaTypes.BOOL)
                .column("short", JavaTypes.SHORT)
                .column("int", JavaTypes.INT)
                .column("float", JavaTypes.FLOAT)
                .column("double", JavaTypes.DOUBLE)
                .column("long", JavaTypes.LONG)
                .column("string", JavaTypes.STRING)
                .column("char", JavaTypes.CHAR)

                .column("date", JavaTypes.DATE)
                .column("color", JavaTypes.COLOR)
                .column("uuid", JavaTypes.UUID)

                .column("vec3i", MinecraftTypes.VEC3I)
                .column("blockpos", MinecraftTypes.BLOCKPOS)
                .column("chunkpos", MinecraftTypes.CHUNKPOS)
                .column("json", MinecraftTypes.JSON)
                .column("nbt", MinecraftTypes.NBT)
                .column("text", MinecraftTypes.TEXT)
                .column("identifier", MinecraftTypes.IDENTIFIER)
                .column("sound", MinecraftTypes.SOUND)

                .column("key", AdventureTypes.KEY)
                .column("component", AdventureTypes.COMPONENT)
                .finish();

        DataContainer container = table.createDataContainer();

        // Test Byte
        container.put(JavaTypes.BYTE, "byte", Byte.MAX_VALUE);
        assertEquals(Byte.MAX_VALUE, container.get(JavaTypes.BYTE, "byte"));
        container.put(JavaTypes.BYTE, "byte", Byte.MIN_VALUE);
        assertEquals(Byte.MIN_VALUE, container.get(JavaTypes.BYTE, "byte"));
        container.put(JavaTypes.BYTE, "byte", (byte) 0);
        assertEquals((byte) 0, container.get(JavaTypes.BYTE, "byte"));

        // Test Bytes
        /*container.put(ColumnType.BYTES, "bytes", testBytes);
        assertEquals(testBytes, container.get(ColumnType.BYTES, "bytes"));*/

        // Test Bool
        container.put(JavaTypes.BOOL, "bool", true);
        assertTrue(container.get(JavaTypes.BOOL, "bool"));
        container.put(JavaTypes.BOOL, "bool", false);
        assertFalse(container.get(JavaTypes.BOOL, "bool"));

        // Test Short
        container.put(JavaTypes.SHORT, "short", Short.MAX_VALUE);
        assertEquals(Short.MAX_VALUE, container.get(JavaTypes.SHORT, "short"));
        container.put(JavaTypes.SHORT, "short", Short.MIN_VALUE);
        assertEquals(Short.MIN_VALUE, container.get(JavaTypes.SHORT, "short"));
        container.put(JavaTypes.SHORT, "short", (short) 0);
        assertEquals((short) 0, container.get(JavaTypes.SHORT, "short"));

        // Test Int
        container.put(JavaTypes.INT, "int", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, container.get(JavaTypes.INT, "int"));
        container.put(JavaTypes.INT, "int", Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, container.get(JavaTypes.INT, "int"));
        container.put(JavaTypes.INT, "int", 0);
        assertEquals(0, container.get(JavaTypes.INT, "int"));

        // Test Float
        container.put(JavaTypes.FLOAT, "float", Float.MAX_VALUE);
        assertEquals(Float.MAX_VALUE, container.get(JavaTypes.FLOAT, "float"));
        container.put(JavaTypes.FLOAT, "float", Float.MIN_VALUE);
        assertEquals(Float.MIN_VALUE, container.get(JavaTypes.FLOAT, "float"));
        container.put(JavaTypes.FLOAT, "float", 0F);
        assertEquals(0F, container.get(JavaTypes.FLOAT, "float"));

        // Test Double
        container.put(JavaTypes.DOUBLE, "double", Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, container.get(JavaTypes.DOUBLE, "double"));
        container.put(JavaTypes.DOUBLE, "double", Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, container.get(JavaTypes.DOUBLE, "double"));
        container.put(JavaTypes.DOUBLE, "double", 0.0);
        assertEquals(0.0, container.get(JavaTypes.DOUBLE, "double"));

        // Test Long
        container.put(JavaTypes.LONG, "long", Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, container.get(JavaTypes.LONG, "long"));
        container.put(JavaTypes.LONG, "long", Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, container.get(JavaTypes.LONG, "long"));
        container.put(JavaTypes.LONG, "long", 0L);
        assertEquals(0L, container.get(JavaTypes.LONG, "long"));

        // Test String
        container.put(JavaTypes.STRING, "string", "Test");
        assertEquals("Test",  container.get(JavaTypes.STRING, "string"));

        // Test Char
        container.put(JavaTypes.CHAR, "char", 'c');
        assertEquals('c',  container.get(JavaTypes.CHAR, "char"));

        // Test Date
        Date date = new Date();
        container.put(JavaTypes.DATE, "date", date);
        assertEquals(date, container.get(JavaTypes.DATE, "date"));

        // Test Color
        Color color = new Color(1, 2, 3);
        container.put(JavaTypes.COLOR, "color", color);
        assertEquals(color, container.get(JavaTypes.COLOR, "color"));

        // Test UUID
        UUID uuid = UUID.randomUUID();
        container.put(JavaTypes.UUID, "uuid", uuid);
        assertEquals(uuid, container.get(JavaTypes.UUID, "uuid"));

        // Test Vec3i
        Vec3i vec3i = new Vec3i(7, 8, 9);
        container.put(MinecraftTypes.VEC3I, "vec3i", vec3i);
        assertEquals(vec3i, container.get(MinecraftTypes.VEC3I, "vec3i"));

        // Test BlockPos
        BlockPos blockPos = new BlockPos(7, 8, 9);
        container.put(MinecraftTypes.BLOCKPOS, "blockpos", blockPos);
        assertEquals(blockPos, container.get(MinecraftTypes.BLOCKPOS, "blockpos"));

        // Test ChunkPos
        ChunkPos chunkPos = new ChunkPos(1, 2);
        container.put(MinecraftTypes.CHUNKPOS, "chunkpos", chunkPos);
        assertEquals(chunkPos, container.get(MinecraftTypes.CHUNKPOS, "chunkpos"));

        // Test JSON
        JsonObject jsonElement = new JsonObject();
        container.put(MinecraftTypes.JSON, "json", jsonElement);
        assertEquals(jsonElement, container.get(MinecraftTypes.JSON, "json"));

        // Test NBT
        NbtCompound nbtCompound = new NbtCompound();
        container.put(MinecraftTypes.NBT, "nbt", nbtCompound);
        assertEquals(nbtCompound, container.get(MinecraftTypes.NBT, "nbt"));

        // Test Text
        Text text = Text.of("Test");
        container.put(MinecraftTypes.TEXT, "text", text);
        assertEquals(text, container.get(MinecraftTypes.TEXT, "text"));

        // Test Identifier
        Identifier identifier = Identifier.tryParse("cool:guys");
        container.put(MinecraftTypes.IDENTIFIER, "identifier", identifier);
        assertEquals(identifier, container.get(MinecraftTypes.IDENTIFIER, "identifier"));

        SoundEvent soundEvent = SoundEvents.BLOCK_BARREL_OPEN;
        container.put(MinecraftTypes.SOUND, "sound", soundEvent);
        assertEquals(soundEvent.getId(), container.get(MinecraftTypes.SOUND, "sound").getId());

        // Test Adventure Key
        Key key = Key.key("cool:guys");
        container.put(AdventureTypes.KEY, "key", key);
        assertEquals(key, container.get(AdventureTypes.KEY, "key"));

        // Test Adventure Component
        Component component = MiniMessage.miniMessage().deserialize("test");
        container.put(AdventureTypes.COMPONENT, "component", component);
        assertEquals(component, container.get(AdventureTypes.COMPONENT, "component"));
    }

    private void testTableFunctions() {
        Table table = SQLib.getDatabase().createTable("test", "table2")
                .column("test", JavaTypes.STRING)
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