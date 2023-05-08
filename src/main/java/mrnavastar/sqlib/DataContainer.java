package mrnavastar.sqlib;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mrnavastar.sqlib.sql.SQLConnection;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.UUID;

public class DataContainer {

    private final Table table;
    private final SQLConnection sqlConnection;
    private final String id;

    public DataContainer(String id, Table table, SQLConnection sqlConnection) {
        this.id = id;
        this.table = table;
        this.sqlConnection = sqlConnection;
    }

    public String getIdAsString() {
        return id;
    }

    public UUID getIdAsUUID() {
        return UUID.fromString(id);
    }

    public int getIdAsInt() {
        return Integer.parseInt(id);
    }

    public void put(String field, String value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, int value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, double value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, long value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, boolean value) {
        sqlConnection.writeField(table, id, field, value ? 1 : 0); // Convert bool to int, SQLite compat
    }

    public void put(String field, BlockPos value) {
        sqlConnection.writeField(table, id, field, value.asLong());
    }

    public void put(String field, ChunkPos value) {
        sqlConnection.writeField(table, id, field, value.toLong());
    }

    public void put(String field, JsonElement value) {
        sqlConnection.writeField(table, id, field, value.toString());
    }

    public void put(String field, NbtElement value) {
        sqlConnection.writeField(table, id, field, value.asString());
    }

    public void put(String field, MutableText value) {
        sqlConnection.writeField(table, id, field, MutableText.Serializer.toJson(value));
    }

    public void put(String field, UUID value) {
        sqlConnection.writeField(table, id, field, value.toString());
    }

    public String getString(String field) {
        return sqlConnection.readField(table, id, field, String.class);
    }

    public int getInt(String field) {
        return sqlConnection.readField(table, id, field, Integer.class);
    }

    public double getDouble(String field) {
        return sqlConnection.readField(table, id, field, Double.class);
    }

    public double getLong(String field) {
        return sqlConnection.readField(table, id, field, Long.class);
    }

    public boolean getBool(String field) {
        return sqlConnection.readField(table, id, field, Integer.class) > 0; //Int to bool, SQLite compat
    }

    public BlockPos getBlockPos(String field) {
        return BlockPos.fromLong(sqlConnection.readField(table, id, field, Long.class));
    }

    public ChunkPos getChunkPos(String field) {
        return new ChunkPos(sqlConnection.readField(table, id, field, Long.class));
    }

    public JsonElement getJson(String field) {
        return SQLib.jsonParser.parse(sqlConnection.readField(table, id, field, String.class));
    }

    public NbtElement getNbt(String field) {
        try {
            return StringNbtReader.parse(sqlConnection.readField(table, id, field, String.class));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MutableText getMutableText(String field) {
        return Text.Serializer.fromJson(sqlConnection.readField(table, id, field, String.class));
    }

    public UUID getUUID(String field) {
        return UUID.fromString(sqlConnection.readField(table, id, field, String.class));
    }
}