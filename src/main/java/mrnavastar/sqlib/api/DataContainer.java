package mrnavastar.sqlib.api;

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
        SqlManager.createRow(this.tableName, this.id);
        JsonObject obj = SqlManager.readJson(this.tableName, this.id, type);
        if (obj != null) {
            obj.addProperty(key, value.toString());
            SqlManager.writeJson(this.tableName, id, type, obj);
        }
        Database.disconnect();
    }

    public void put(String key, String value) {
        putIntoDatabase("STRINGS", key, value);
    }

    public void put(String key, int value) {
        putIntoDatabase("INTS", key, value);
    }

    public void put(String key, boolean value) {
        putIntoDatabase("BOOLEANS", key, value);
    }

    public void put(String key, JsonObject value) {
        putIntoDatabase("JSON", key, value);
    }

    public void put(String key, NbtCompound value) {
        putIntoDatabase("NBT", key, value);
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

    public void dropBoolean(String key) {
        dropFromDatabase("BOOLEANS", key);
    }

    public void dropJson(String key) {
        dropFromDatabase("JSON", key);
    }

    public void dropNbt(String key) {
        dropFromDatabase("NBT", key);
    }

    private JsonElement getFromDatabase(String type, String key) {
        Database.connect();
        JsonObject obj = SqlManager.readJson(this.tableName, this.id, type);
        Database.disconnect();
        assert obj != null;
        return obj.get(key);
    }

    public String getString(String key) {
        return getFromDatabase("STRINGS", key).getAsString();
    }

    public int getInt(String key) {
       return getFromDatabase("INTS", key).getAsInt();
    }

    public boolean getBoolean(String key) {
        return getFromDatabase("BOOLEANS", key).getAsBoolean();
    }

    public JsonObject getJson(String key) {
        return getFromDatabase("JSON", key).getAsJsonObject();
    }

    public NbtCompound getNbt(String key) {
        try {
            return StringNbtReader.parse(getFromDatabase("NBT", key).getAsString());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}