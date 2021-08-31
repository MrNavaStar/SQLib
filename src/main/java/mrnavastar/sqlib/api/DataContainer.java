package mrnavastar.sqlib.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mrnavastar.sqlib.util.Database;
import mrnavastar.sqlib.util.SqlManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

public class DataContainer {

    private String tableName;
    private final String id;

    public DataContainer(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTableName(String name) {
        this.tableName = name;
    }

    private void putIntoDatabase(String type, String key, Object value) {
        Database.connect();
        JsonObject obj = SqlManager.readJson(this.tableName, this.id, type);
        if (obj == null) obj = new JsonObject();
        else obj.remove(key);
        obj.addProperty(key, value.toString());
        SqlManager.writeJson(this.tableName, this.id, type, obj);
        Database.disconnect();
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
        putIntoDatabase("JSON", key, value);
    }


    public void put(String key, NbtCompound value) {
        putIntoDatabase("NBT_COMPOUNDS", key, value);
    }

    private void dropFromDatabase(String type, String key) {
        Database.connect();
        JsonObject obj = SqlManager.readJson(this.tableName, this.id, type);
        if (obj != null) {
            obj.remove(key);
            SqlManager.writeJson(this.tableName, id, type, obj);
        }
        Database.disconnect();
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
        dropFromDatabase("NBT_COMPOUNDS", key);
    }

    public void dropNbtList(String key) {
        dropFromDatabase("NBT_LISTS", key);
    }

    private JsonElement getFromDatabase(String type, String key) {
        Database.connect();
        JsonObject obj = SqlManager.readJson(this.tableName, this.id, type);
        Database.disconnect();
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
        try {
            return StringNbtReader.parse(getFromDatabase("NBT_COMPOUNDS", key).getAsString());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}