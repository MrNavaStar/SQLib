package me.mrnavastar.sqlib;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.mrnavastar.easyeula.EasyEula;
import me.mrnavastar.sqlib.api.DataContainer;
import me.mrnavastar.sqlib.api.types.AdventureTypes;
import me.mrnavastar.sqlib.api.types.JavaTypes;
import me.mrnavastar.sqlib.api.types.MinecraftTypes;
import me.mrnavastar.sqlib.api.types.GsonTypes;
import me.mrnavastar.sqlib.api.DataStore;
import me.mrnavastar.sqlib.impl.SoundParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.Arrays;
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
        DataContainer container = SQLib.getDatabase().dataStore("test", "store1").createContainer();

        // Test Byte
        container.put(JavaTypes.BYTE, "byte", Byte.MAX_VALUE);
        assertEquals(Byte.MAX_VALUE, container.get(JavaTypes.BYTE, "byte").orElseThrow());
        container.put(JavaTypes.BYTE, "byte", Byte.MIN_VALUE);
        assertEquals(Byte.MIN_VALUE, container.get(JavaTypes.BYTE, "byte").orElseThrow());
        container.put(JavaTypes.BYTE, "byte", (byte) 0);
        assertEquals((byte) 0, container.get(JavaTypes.BYTE, "byte").orElseThrow());

        // Test Bytes
        byte[] bytes = new byte[]{1, 2, 3, 4};
        container.put(JavaTypes.BYTES, "bytes", bytes);
        if (!Arrays.equals(bytes, container.get(JavaTypes.BYTES, "bytes").orElseThrow())) {
            throw new RuntimeException("Expected bytes, got " + Arrays.toString(bytes));
        }

        // Test Bool
        container.put(JavaTypes.BOOL, "bool", true);
        assertTrue(container.get(JavaTypes.BOOL, "bool").orElseThrow());
        container.put(JavaTypes.BOOL, "bool", false);
        assertFalse(container.get(JavaTypes.BOOL, "bool").orElseThrow());

        // Test Short
        container.put(JavaTypes.SHORT, "short", Short.MAX_VALUE);
        assertEquals(Short.MAX_VALUE, container.get(JavaTypes.SHORT, "short").orElseThrow());
        container.put(JavaTypes.SHORT, "short", Short.MIN_VALUE);
        assertEquals(Short.MIN_VALUE, container.get(JavaTypes.SHORT, "short").orElseThrow());
        container.put(JavaTypes.SHORT, "short", (short) 0);
        assertEquals((short) 0, container.get(JavaTypes.SHORT, "short").orElseThrow());

        // Test Int
        container.put(JavaTypes.INT, "int", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, container.get(JavaTypes.INT, "int").orElseThrow());
        container.put(JavaTypes.INT, "int", Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, container.get(JavaTypes.INT, "int").orElseThrow());
        container.put(JavaTypes.INT, "int", 0);
        assertEquals(0, container.get(JavaTypes.INT, "int").orElseThrow());

        // Test Float
        container.put(JavaTypes.FLOAT, "float", Float.MAX_VALUE);
        assertEquals(Float.MAX_VALUE, container.get(JavaTypes.FLOAT, "float").orElseThrow());
        container.put(JavaTypes.FLOAT, "float", Float.MIN_VALUE);
        assertEquals(Float.MIN_VALUE, container.get(JavaTypes.FLOAT, "float").orElseThrow());
        container.put(JavaTypes.FLOAT, "float", 0F);
        assertEquals(0F, container.get(JavaTypes.FLOAT, "float").orElseThrow());

        // Test Double
        container.put(JavaTypes.DOUBLE, "double", Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, container.get(JavaTypes.DOUBLE, "double").orElseThrow());
        container.put(JavaTypes.DOUBLE, "double", Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, container.get(JavaTypes.DOUBLE, "double").orElseThrow());
        container.put(JavaTypes.DOUBLE, "double", 0.0);
        assertEquals(0.0, container.get(JavaTypes.DOUBLE, "double").orElseThrow());

        // Test Long
        container.put(JavaTypes.LONG, "long", Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, container.get(JavaTypes.LONG, "long").orElseThrow());
        container.put(JavaTypes.LONG, "long", Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, container.get(JavaTypes.LONG, "long").orElseThrow());
        container.put(JavaTypes.LONG, "long", 0L);
        assertEquals(0L, container.get(JavaTypes.LONG, "long").orElseThrow());

        // Test String
        container.put(JavaTypes.STRING, "string", "Test");
        assertEquals("Test",  container.get(JavaTypes.STRING, "string").orElseThrow());

        // Test Char
        container.put(JavaTypes.CHAR, "char", 'c');
        assertEquals('c',  container.get(JavaTypes.CHAR, "char").orElseThrow());

        // Test Date
        Date date = new Date();
        container.put(JavaTypes.DATE, "date", date);
        assertEquals(date, container.get(JavaTypes.DATE, "date").orElseThrow());

        // Test Color
        Color color = new Color(1, 2, 3);
        container.put(JavaTypes.COLOR, "color", color);
        assertEquals(color, container.get(JavaTypes.COLOR, "color").orElseThrow());

        // Test UUID
        UUID uuid = UUID.randomUUID();
        container.put(JavaTypes.UUID, "uuid", uuid);
        assertEquals(uuid, container.get(JavaTypes.UUID, "uuid").orElseThrow());

        // Test Vec3i
        Vec3i vec3i = new Vec3i(7, 8, 9);
        container.put(MinecraftTypes.VEC3I, "vec3i", vec3i);
        assertEquals(vec3i, container.get(MinecraftTypes.VEC3I, "vec3i").orElseThrow());

        // Test BlockPos
        BlockPos blockPos = new BlockPos(7, 8, 9);
        container.put(MinecraftTypes.BLOCKPOS, "blockpos", blockPos);
        assertEquals(blockPos, container.get(MinecraftTypes.BLOCKPOS, "blockpos").orElseThrow());

        // Test ChunkPos
        ChunkPos chunkPos = new ChunkPos(1, 2);
        container.put(MinecraftTypes.CHUNKPOS, "chunkpos", chunkPos);
        assertEquals(chunkPos, container.get(MinecraftTypes.CHUNKPOS, "chunkpos").orElseThrow());

        // Test JSON
        JsonObject jsonElement = new JsonObject();
        container.put(GsonTypes.OBJECT, "json", jsonElement);
        assertEquals(jsonElement, container.get(GsonTypes.OBJECT, "json").orElseThrow());

        // Test NBT
        NbtCompound nbtCompound = new NbtCompound();
        container.put(MinecraftTypes.NBT, "nbt", nbtCompound);
        assertEquals(nbtCompound, container.get(MinecraftTypes.NBT, "nbt").orElseThrow());

        // Test Text
        Text text = Text.of("Test");
        container.put(MinecraftTypes.TEXT, "text", text);
        assertEquals(text, container.get(MinecraftTypes.TEXT, "text").orElseThrow());

        // Test Identifier
        Identifier identifier = Identifier.tryParse("cool:guys");
        container.put(MinecraftTypes.IDENTIFIER, "identifier", identifier);
        assertEquals(identifier, container.get(MinecraftTypes.IDENTIFIER, "identifier").orElseThrow());

        SoundEvent soundEvent = SoundEvents.BLOCK_BARREL_OPEN;
        container.put(MinecraftTypes.SOUND, "sound", soundEvent);
        assertEquals(SoundParser.getId(soundEvent), SoundParser.getId(container.get(MinecraftTypes.SOUND, "sound").orElseThrow()));

        // Test Adventure Key
        Key key = Key.key("cool:guys");
        container.put(AdventureTypes.KEY, "key", key);
        assertEquals(key, container.get(AdventureTypes.KEY, "key").orElseThrow());

        // Test Adventure Component
        Component component = MiniMessage.miniMessage().deserialize("test");
        container.put(AdventureTypes.COMPONENT, "component", component);
        assertEquals(component, container.get(AdventureTypes.COMPONENT, "component").orElseThrow());

        // Test Transaction
        container.transaction()
                .put(JavaTypes.STRING, "gamer", "epic")
                .put(MinecraftTypes.IDENTIFIER, "pog", Identifier.tryParse("pog:champ"))
                .put(JavaTypes.BOOL, "what", false)
                .commit();
    }

    private void testStoreFunctions() {
        DataStore store = SQLib.getDatabase().dataStore("test", "store1");

        System.out.println(store.getKeys());
    }

    @Override
    @SneakyThrows
    public void onInitialize() {
        EasyEula.acceptEula();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> server.stop(false));

        System.out.println("---------- Starting Tests ----------");
        System.out.println("Starting Transactions");
        testAllTransactions();
        System.out.println("Starting Store Functions");
        testStoreFunctions();
        System.out.println("--------------- Done ----------------");
    }
}
