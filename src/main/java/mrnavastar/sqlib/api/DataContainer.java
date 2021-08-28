package mrnavastar.sqlib.api;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

public class DataContainer {

    private final String id;
    private final JsonObject strings;
    private final JsonObject ints;
    private final JsonObject booleans;
    public final JsonObject jsonObjects;

    private final JsonObject nbt;

    public DataContainer(String id) {
        this.id = id;
        this.strings = new JsonObject();
        this.ints = new JsonObject();
        this.booleans = new JsonObject();
        this.jsonObjects = new JsonObject();

        this.nbt = new JsonObject();
    }

    public DataContainer(JsonObject obj) {
        this.id = obj.get("ID").getAsString();
        this.strings = obj.getAsJsonObject("STRINGS");
        this.ints = obj.getAsJsonObject("INTS");
        this.booleans = obj.getAsJsonObject("BOOLEANS");
        this.jsonObjects = obj.getAsJsonObject("JSON");

        this.nbt = obj.getAsJsonObject("NBT");
    }

    public String getId() {
        return this.id;
    }

    public JsonObject getStrings() {
        return this.strings;
    }

    public JsonObject getInts() {
        return this.ints;
    }

    public JsonObject getBooleans() {
        return this.booleans;
    }

    public JsonObject getJsonObjects() {
        return this.jsonObjects;
    }

    public JsonObject getNbts() {
        return this.nbt;
    }

    public void put(String key, String value) {
        this.strings.addProperty(key, value);
    }

    public void put(String key, int value) {
        this.ints.addProperty(key, value);
    }

    public void put(String key, boolean value) {
        this.booleans.addProperty(key, value);
    }

    public void put(String key, JsonObject value) {
        this.jsonObjects.add(key, value);
    }

    public void put(String key, NbtCompound value) {
        this.nbt.addProperty(key, value.toString());
    }

    public void dropString(String key) {
        this.strings.remove(key);
    }

    public void dropInt(String key) {
        this.ints.remove(key);
    }

    public void dropBoolean(String key) {
        this.booleans.remove(key);
    }

    public void dropJson(String key) {
        this.jsonObjects.remove(key);
    }

    public void dropNbt(String key) {
        this.nbt.remove(key);
    }

    public String getString(String key) {
        return this.strings.get(key).getAsString();
    }

    public int getInt(String key) {
        return this.ints.get(key).getAsInt();
    }

    public boolean getBoolean(String key) {
        return this.booleans.get(key).getAsBoolean();
    }

    public JsonObject getJson(String key) {
        return this.jsonObjects.getAsJsonObject(key);
    }

    public NbtCompound getNbt(String key) {
        try {
            return StringNbtReader.parse(nbt.get(key).getAsString());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}