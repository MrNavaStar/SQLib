package mrnavastar.sqlib.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mrnavastar.sqlib.api.databases.Database;
import mrnavastar.sqlib.util.Parser;
import mrnavastar.sqlib.util.SqlManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class DataContainer {

    private Table table;
    private Database database;
    private final String id;

    public DataContainer(String id) {
        this.id = id;
    }

    public DataContainer(UUID id) {
        this.id = id.toString();
    }

    public DataContainer(int id) {
        this.id = String.valueOf(id);
    }

    public String getId() {
        return this.id;
    }

    public UUID getIdAsUuid() {
        return UUID.fromString(this.id);
    }

    public int getIdAsInt() {
        return Integer.parseInt(this.id);
    }

    public void link(Table table, Database database) {
        this.table = table;
        this.database = database;
    }

    private void putIntoDatabase(String type, String key, Object value) {
        if (!this.table.isInTransaction()) this.database.connect();
        JsonObject obj = SqlManager.readJson(this.table.getName(), this.id, type);
        if (obj == null) obj = new JsonObject();
        else obj.remove(key);
        if (type.equals("JSON")) obj.add(key, (JsonElement) value);
        else obj.addProperty(key, value.toString());
        SqlManager.writeJson(this.table.getName(), this.id, type, obj);
        if (!this.table.isInTransaction()) this.database.disconnect();
    }

    private void dropFromDatabase(String type, String key) {
        if (!this.table.isInTransaction()) this.database.connect();
        JsonObject obj = SqlManager.readJson(this.table.getName(), this.id, type);
        if (obj != null) {
            obj.remove(key);
            SqlManager.writeJson(this.table.getName(), this.id, type, obj);
        }
        if (!this.table.isInTransaction()) this.database.disconnect();
    }

    private JsonElement getFromDatabase(String type, String key) {
        if (!this.table.isInTransaction()) this.database.connect();
        JsonObject obj = SqlManager.readJson(this.table.getName(), this.id, type);
        if (!this.table.isInTransaction()) this.database.disconnect();
        if (obj == null) return null;
        return obj.get(key);
    }

    public void put(String key, String value) {
        putIntoDatabase("STRINGS", key, value);
    }

    public void put(String key, String[] value) {
        putIntoDatabase("STRING_ARRAYS", key, value);
    }

    public void put(String key, int value) {
        putIntoDatabase("INTS", key, value);
    }

    public void put(String key, int[] value) {
        putIntoDatabase("INT_ARRAYS", key, value);
    }

    public void put(String key, float value) {
        putIntoDatabase("FLOATS", key, value);
    }

    public void put(String key, double value) {
        putIntoDatabase("DOUBLES", key, value);
    }

    public void put(String key, boolean value) {
        putIntoDatabase("BOOLEANS", key, value);
    }

    public void put(String key, JsonElement value) {
        putIntoDatabase("JSON", key, value);
    }

    public void put(String key, NbtElement value) {
        if (!this.table.isInTransaction()) this.database.connect();
        JsonElement json = getFromDatabase("NBT", "DATA");
        NbtCompound nbt;
        if (json != null) nbt = Parser.nbtFromString(json.getAsString());
        else nbt = new NbtCompound();

        //should only be false if the data in the database is bad
        if (nbt != null) {
            nbt.put(key, value);
            putIntoDatabase("NBT", "DATA", nbt);
        }
        if (!this.table.isInTransaction()) this.database.disconnect();
    }

    public void put(String key, BlockPos value) {
        putIntoDatabase("BLOCK_POS", key, value.toShortString());
    }

    public void put(String key, BlockPos[] value) {
        putIntoDatabase("BLOCK_POS_ARRAY", key, value);
    }

    public void put(String key, UUID value) {
        putIntoDatabase("UUIDS", key, value);
    }

    public void put(String key, UUID[] value) {
        putIntoDatabase("UUID_ARRAYS", key, value);
    }

    public void put(String key, LiteralText value) {
        putIntoDatabase("LITERAL_TEXTS", key, value);
    }

    public void put(String key, MutableText value) {
        putIntoDatabase("MUTABLE_TEXTS", key, value);
    }

    public void dropString(String key) {
        dropFromDatabase("STRINGS", key);
    }

    public void dropStringArray(String key) {
        dropFromDatabase("STRING_ARRAYS", key);
    }

    public void dropInt(String key) {
        dropFromDatabase("INTS", key);
    }

    public void dropIntArray(String key) {
        dropFromDatabase("INT_ARRAY", key);
    }

    public void dropFloat(String key) {
        dropFromDatabase("FLOATS", key);
    }

    public void dropDouble(String key) {
        dropFromDatabase("DOUBLES", key);
    }

    public void dropBoolean(String key) {
        dropFromDatabase("BOOLEANS", key);
    }

    public void dropJson(String key) {
        dropFromDatabase("JSON", key);
    }

    public void dropNbt(String key) {
        if (!this.table.isInTransaction()) this.database.connect();
        JsonElement json = getFromDatabase("NBT", "DATA");
        if (json != null) {
            NbtCompound nbt = Parser.nbtFromString(json.getAsString());

            //should only be false if the data in the database is bad
            if (nbt != null && nbt.contains(key)) {
                nbt.remove(key);
                putIntoDatabase("NBT", "DATA", nbt);
            }
        }
        if (!this.table.isInTransaction()) this.database.disconnect();
    }

    public void dropBlockPos(String key) {
        dropFromDatabase("BLOCK_POS", key);
    }

    public void dropBlockPosArray(String key) {
        dropFromDatabase("BLOCK_POS_ARRAY", key);
    }

    public void dropUuid(String key) {
        dropFromDatabase("UUIDS", key);
    }

    public void dropUuidArray(String key) {
        dropFromDatabase("UUID_ARRAYS", key);
    }

    public void dropLiteralText(String key) {
        dropFromDatabase("LITERAL_TEXTS", key);
    }

    public void dropMutableText(String key) {
        dropFromDatabase("MUTABLE_TEXTS", key);
    }

    public String getString(String key) {
        JsonElement json = getFromDatabase("STRINGS", key);
        if (json != null) return json.getAsString();
        return null;
    }

    public String[] getStringArray(String key) {
        JsonElement json = getFromDatabase("STRING_ARRAYS", key);
        if (json != null) return Parser.stringArrayFromString(json.getAsString());
        return null;
    }

    public int getInt(String key) {
        JsonElement json = getFromDatabase("INTS", key);
        if (json != null) return json.getAsInt();
        return -0;
    }

    public int[] getIntArray(String key) {
        JsonElement json = getFromDatabase("INT_ARRAY", key);
        if (json != null) return Parser.intArrayFromString(json.getAsString());
        return null;
    }

    public float getFloat(String key) {
        JsonElement json = getFromDatabase("FLOATS", key);
        if (json != null) return json.getAsFloat();
        return -0;
    }

    public double getDouble(String key) {
        JsonElement json = getFromDatabase("DOUBLES", key);
        if (json != null) return json.getAsDouble();
        return -0;
    }

    public boolean getBoolean(String key) {
        JsonElement json = getFromDatabase("BOOLEANS", key);
        if (json != null) return json.getAsBoolean();
        return false;
    }

    public JsonElement getJson(String key) {
        return getFromDatabase("JSON", key);
    }

    public NbtElement getNbt(String key) {
        JsonElement json = getFromDatabase("NBT", "DATA");
        if (json != null) {
            NbtCompound nbt = Parser.nbtFromString(json.getAsString());

            //should only be false if the data in the database is bad
            if (nbt == null || !nbt.contains(key)) return null;
            return nbt.get(key);
        }
        return null;
    }

    public BlockPos getBlockPos(String key) {
        JsonElement json = getFromDatabase("BLOCK_POS", key);
        if (json != null) return Parser.blockPosFromString(json.getAsString());
        return null;
    }

    public BlockPos[] getBlockPosArray(String key) {
        JsonElement json = getFromDatabase("BLOCK_POS_ARRAYS", key);
        if (json != null) return Parser.blockPosArrayFromString(json.getAsString());
        return null;
    }

    public UUID getUuid(String key) {
        JsonElement json = getFromDatabase("UUIDS", key);
        if (json != null) return UUID.fromString(json.getAsString());
        return null;
    }

    public UUID[] getUuidArray(String key) {
        JsonElement json = getFromDatabase("UUIDS", key);
        if (json != null) return Parser.uuidArrayFromString(json.getAsString());
        return null;
    }

    public LiteralText getLiteralText(String key) {
        JsonElement json = getFromDatabase("LITERAL_TEXTS", key);
        if (json != null) return new LiteralText(json.getAsString());
        return null;
    }

    public MutableText getMutableText(String key) {
        return Text.Serializer.fromJson(getFromDatabase("MUTABLE_TEXTS", key));
    }
}