package mrnavastar.sqlib.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mrnavastar.sqlib.util.Database;
import mrnavastar.sqlib.util.Parser;
import mrnavastar.sqlib.util.SqlManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class DataContainer {

    private Table table;
    private final String id;

    public DataContainer(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    private void putIntoDatabase(String type, String key, Object value) {
        if (!this.table.isInTransaction()) Database.connect();
        JsonObject obj = SqlManager.readJson(this.table.getName(), this.id, type);
        if (obj == null) obj = new JsonObject();
        else obj.remove(key);
        obj.addProperty(key, value.toString());
        SqlManager.writeJson(this.table.getName(), this.id, type, obj);
        if (!this.table.isInTransaction()) Database.disconnect();
    }

    public void put(String key, String value) {
        putIntoDatabase("STRINGS", key, value);
    }

    public void put(String key, int value) {
        putIntoDatabase("INTS", key, value);
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
        if (!this.table.isInTransaction()) Database.connect();
        JsonObject obj = SqlManager.readJson(this.table.getName(), this.id, "JSON");
        if (obj == null) obj = new JsonObject();
        else obj.remove(key);
        obj.add(key, value);
        SqlManager.writeJson(this.table.getName(), this.id, "JSON", obj);
        if (!this.table.isInTransaction()) Database.disconnect();
    }

    public void put(String key, NbtCompound value) {
        putIntoDatabase("NBT", key, value);
    }

    public void put(String key, BlockPos value) {
        putIntoDatabase("BLOCKPOS", key, value);
    }

    private void dropFromDatabase(String type, String key) {
        if (!this.table.isInTransaction()) Database.connect();
        JsonObject obj = SqlManager.readJson(this.table.getName(), this.id, type);
        if (obj != null) {
            obj.remove(key);
            SqlManager.writeJson(this.table.getName(), id, type, obj);
        }
        if (!this.table.isInTransaction()) Database.disconnect();
    }

    public void dropString(String key) {
        dropFromDatabase("STRINGS", key);
    }

    public void dropInt(String key) {
        dropFromDatabase("INTS", key);
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

    public void dropNbtCompound(String key) {
        dropFromDatabase("NBT", key);
    }

    public void dropBlockPos(String key) {
        dropFromDatabase("BLOCKPOS", key);
    }

    private JsonElement getFromDatabase(String type, String key) {
        if (!this.table.isInTransaction()) Database.connect();
        JsonObject obj = SqlManager.readJson(this.table.getName(), this.id, type);
        if (!this.table.isInTransaction()) Database.disconnect();
        if (obj == null) obj = new JsonObject();
        return obj.get(key);
    }

    public String getString(String key) {
        return getFromDatabase("STRINGS", key).getAsString();
    }

    public int getInt(String key) {
       return getFromDatabase("INTS", key).getAsInt();
    }

    public float getFloat(String key) {
        return getFromDatabase("FLOATS", key).getAsFloat();
    }

    public double getDouble(String key) {
        return getFromDatabase("DOUBLES", key).getAsDouble();
    }

    public boolean getBoolean(String key) {
        return getFromDatabase("BOOLEANS", key).getAsBoolean();
    }

    public JsonElement getJson(String key) {
        return getFromDatabase("JSON", key);
    }

    public NbtCompound getNbtCompound(String key) {
        return Parser.nbtFromString(getFromDatabase("NBT", key).getAsString());
    }

    public BlockPos getBlockPos(String key) {
        return Parser.blockPosFromString(getFromDatabase("BLOCKPOS", key).getAsString());
    }
}