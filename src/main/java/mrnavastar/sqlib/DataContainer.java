package mrnavastar.sqlib;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mrnavastar.sqlib.sql.SQLConnection;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.sql.Date;
import java.sql.Time;
import java.time.Year;
import java.util.UUID;

public class DataContainer {

    private Table table;
    private SQLConnection sqlConnection;
    private final String id;

    public DataContainer(String id, Table table, SQLConnection sqlConnection) {
        this.id = id;
        this.table = table;
        this.sqlConnection = sqlConnection;
    }

    public String getId() {
        return this.id;
    }

    public void put(String field, String value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, int value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, float value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, double value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, boolean value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, Date value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, Time value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, Year value) {
        sqlConnection.writeField(table, id, field, value);
    }

    public void put(String field, BlockPos value) {
        sqlConnection.writeField(table, id, field, value.asLong());
    }

    public void put(String field, ChunkPos value) {
        sqlConnection.writeField(table, id, field, value.toLong());
    }

    public void put(String field, JsonElement value) {
        sqlConnection.writeField(table, id, field, value.getAsString());
    }

    public void put(String field, NbtElement value) {
        sqlConnection.writeField(table, id, field, value.asString());
    }

    public void put(String field, UUID value) {
        sqlConnection.writeField(table, id, field, value.toString());
    }

    public String getString(String field) {
        return sqlConnection.readField(table, id, field, String.class);
    }

    public int getInt(String field) {
        return sqlConnection.readField(table, id, field, int.class);
    }

    public float getFloat(String field) {
        return sqlConnection.readField(table, id, field, float.class);
    }

    public double getDouble(String field) {
        return sqlConnection.readField(table, id, field, double.class);
    }

    public boolean getBool(String field) {
        return sqlConnection.readField(table, id, field, boolean.class);
    }

    public Date getDate(String field) {
        return sqlConnection.readField(table, id, field, Date.class);
    }

    public Time getTime(String field) {
        return sqlConnection.readField(table, id, field, Time.class);
    }

    public Year getYear(String field) {
        return sqlConnection.readField(table, id, field, Year.class);
    }

    public BlockPos getBlockPos(String field) {
        return BlockPos.fromLong(sqlConnection.readField(table, id, field, long.class));
    }

    public ChunkPos getChunkPos(String field) {
        return new ChunkPos(sqlConnection.readField(table, id, field, long.class));
    }

    public JsonElement getJson(String field) {
        return SQLib.GSON.toJsonTree(sqlConnection.readField(table, id, field, String.class));
    }

    public NbtElement getNbt(String field) {
        try {
            return StringNbtReader.parse(sqlConnection.readField(table, id, field, String.class));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UUID getUUID(String field) {
        return UUID.fromString(sqlConnection.readField(table, id, field, String.class));
    }
}