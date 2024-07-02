package me.mrnavastar.sqlib;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.sql.SQLConnection;
import me.mrnavastar.sqlib.util.TextParser;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.awt.*;
import java.util.Date;
import java.util.UUID;

/**
 * This class represents a row in the {@link Table} in the {@link me.mrnavastar.sqlib.database.Database}
 */
@AllArgsConstructor
public class DataContainer {

    @NonNull private final String id;
    @NonNull private final Table table;
    @NonNull private final SQLConnection sqlConnection;

    public String getIdAsString() {
        return id;
    }

    public UUID getIdAsUUID() {
        return UUID.fromString(id);
    }

    public int getIdAsInt() {
        return Integer.parseInt(id);
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, String value) {
        sqlConnection.writeField(table, id, field, value);
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, int value) {
        sqlConnection.writeField(table, id, field, value);
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, double value) {
        sqlConnection.writeField(table, id, field, value);
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, long value) {
        sqlConnection.writeField(table, id, field, value);
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, boolean value) {
        sqlConnection.writeField(table, id, field, value ? 1 : 0); // Convert bool to int, SQLite compat
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, Date value) {
        sqlConnection.writeField(table, id, field, value.getTime());
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, Color value) {
        sqlConnection.writeField(table, id, field, value.getRGB());
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, @NonNull BlockPos value) {
        sqlConnection.writeField(table, id, field, value.asLong());
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, @NonNull ChunkPos value) {
        sqlConnection.writeField(table, id, field, value.toLong());
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, @NonNull JsonElement value) {
        sqlConnection.writeField(table, id, field, value.toString());
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, @NonNull NbtElement value) {
        sqlConnection.writeField(table, id, field, value.asString());
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, @NonNull Text value) {
        sqlConnection.writeField(table, id, field, TextParser.textToString(value));
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, @NonNull UUID value) {
        sqlConnection.writeField(table, id, field, value.toString());
        return this;
    }

    @SneakyThrows
    public DataContainer put(@NonNull String field, @NonNull Identifier value) {
        sqlConnection.writeField(table, id, field, value.toString());
        return this;
    }

    @SneakyThrows
    public String getString(@NonNull String field) {
        return sqlConnection.readField(table, id, field, String.class);
    }

    @SneakyThrows
    public int getInt(@NonNull String field) {
        return sqlConnection.readField(table, id, field, Integer.class);
    }

    @SneakyThrows
    public double getDouble(@NonNull String field) {
        return sqlConnection.readField(table, id, field, Double.class);
    }

    @SneakyThrows
    public long getLong(@NonNull String field) {
        // Has to be done this way because the sql driver converts small long values to ints and makes me cry
        return sqlConnection.readField(table, id, field, Number.class).longValue();
    }

    @SneakyThrows
    public boolean getBool(@NonNull String field) {
        return sqlConnection.readField(table, id, field, Integer.class) > 0; //Int to bool, SQLite compat
    }

    @SneakyThrows
    public Date getDate(@NonNull String field) {
        Long time = sqlConnection.readField(table, id, field, Long.class);
        if (time == null) return null;
        return new Date(time);
    }

    @SneakyThrows
    public Color getColor(@NonNull String field) {
        Integer color = sqlConnection.readField(table, id, field, Integer.class);
        if (color == null) return null;
        return new Color(color);
    }

    @SneakyThrows
    public BlockPos getBlockPos(@NonNull String field) {
        Long pos = sqlConnection.readField(table, id, field, Long.class);
        if (pos == null) return null;
        return BlockPos.fromLong(pos);
    }

    @SneakyThrows
    public ChunkPos getChunkPos(@NonNull String field) {
        Long pos = sqlConnection.readField(table, id, field, Long.class);
        if (pos == null) return null;
        return new ChunkPos(pos);
    }

    @SneakyThrows
    public JsonElement getJson(@NonNull String field) {
        String json = sqlConnection.readField(table, id, field, String.class);
        if (json == null) return null;
        return JsonParser.parseString(json);
    }

    @SneakyThrows
    public NbtElement getNbt(@NonNull String field) {
        String nbt = sqlConnection.readField(table, id, field, String.class);
        if (nbt == null) return null;
        return StringNbtReader.parse(nbt);
    }

    @SneakyThrows
    public Text getText(@NonNull String field) {
        String text = sqlConnection.readField(table, id, field, String.class);
        if (text == null) return null;
        return TextParser.stringToText(text);
    }

    @SneakyThrows
    public MutableText getMutableText(@NonNull String field) {
        return (MutableText) getText(field);
    }

    @SneakyThrows
    public UUID getUUID(@NonNull String field) {
        String uuid = sqlConnection.readField(table, id, field, String.class);
        if (uuid == null) return null;
        return UUID.fromString(uuid);
    }

    @SneakyThrows
    public Identifier getIdentifier(@NonNull String field) {
        String identifier = sqlConnection.readField(table, id, field, String.class);
        if (identifier == null) return null;
        return Identifier.tryParse(identifier);
    }

    @SneakyThrows
    public void clear(@NonNull String field) {
        sqlConnection.writeField(table, id, field, null);
    }
}