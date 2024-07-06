package me.mrnavastar.sqlib.sql;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.mrnavastar.sqlib.util.ByteParser;
import me.mrnavastar.sqlib.util.TextParser;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.awt.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class ColumnType<T> {
    private final SQLPrimitives<?> sqlType;
    private final Function<T, Object> serializer;
    private final Function<Object, T> deserializer;

    public <S> ColumnType(SQLPrimitives<S> sqlType, Function<T, S> serializer, Function<S, T> deserializer) {
        this.sqlType = sqlType;
        this.serializer = (Function<T, Object>) serializer;
        this.deserializer = (Function<Object, T>) deserializer;
    }

    public SQLPrimitives<?> sqlType() {
        return sqlType;
    }

    public Object serialize(T value) {
        return serializer.apply(value);
    }

    public T deserialize(Object value) {
        return deserializer.apply(value);
    }

    /*public ColumnType<List<T>> list() {
        return new ColumnType<>(sqlType,
                v -> {
                    ByteBuf buf = Unpooled.buffer();
                    v.stream().map(this::serialize).forEach(p -> buf.writeBytes(ByteParser.primToBytes(p)));
                    return buf.array();
                },
                v -> {


                    return new ArrayList<>(Collections.singletonList((T) v));
                }
        );
    }*/

    // SQL Primitives
    public static final ColumnType<Byte> BYTE = new ColumnType<>(SQLPrimitives.INT, Byte::intValue, Integer::byteValue);
    public static final ColumnType<byte[]> BYTES = new ColumnType<>(SQLPrimitives.BYTES, v -> v, v -> v);
    public static final ColumnType<Boolean> BOOL = new ColumnType<>(SQLPrimitives.INT, v -> v ? 1 : 0, v -> v == 1);
    public static final ColumnType<Short> SHORT = new ColumnType<>(SQLPrimitives.SHORT, v -> v, v -> v);
    public static final ColumnType<Integer> INT = new ColumnType<>(SQLPrimitives.INT, v -> v, v -> v);
    public static final ColumnType<Float> FLOAT = new ColumnType<>(SQLPrimitives.FLOAT, v -> v, v -> v);
    public static final ColumnType<Double> DOUBLE = new ColumnType<>(SQLPrimitives.DOUBLE, v -> v, v -> v);
    public static final ColumnType<Long> LONG = new ColumnType<>(SQLPrimitives.LONG, v -> v, v -> v);
    public static final ColumnType<String> STRING = new ColumnType<>(SQLPrimitives.STRING, v -> v, v -> v);
    public static final ColumnType<Character> CHAR = new ColumnType<>(SQLPrimitives.CHAR, v -> v, v -> v);
    public static final ColumnType<Date> DATE = new ColumnType<>(SQLPrimitives.DATE, v -> v, v -> v);
    public static final ColumnType<LocalDateTime> DATETIME = new ColumnType<>(SQLPrimitives.DATETIME, v -> v, v -> v);
    public static final ColumnType<Time> TIME = new ColumnType<>(SQLPrimitives.TIME, v -> v, v -> v);
    public static final ColumnType<Timestamp> TIMESTAMP = new ColumnType<>(SQLPrimitives.TIMESTAMP, v -> v, v -> v);

    // Java Data Types
    public static final ColumnType<Color> COLOR = new ColumnType<>(SQLPrimitives.INT, Color::getRGB, Color::new);
    public static final ColumnType<UUID> UUID = new ColumnType<>(SQLPrimitives.STRING, java.util.UUID::toString, java.util.UUID::fromString);

    // Minecraft Data Types
    public static final ColumnType<BlockPos> BLOCKPOS = new ColumnType<>(SQLPrimitives.LONG, BlockPos::asLong, BlockPos::fromLong);
    public static final ColumnType<ChunkPos> CHUNKPOS = new ColumnType<>(SQLPrimitives.LONG, ChunkPos::toLong, ChunkPos::new);
    public static final ColumnType<JsonElement> JSON = new ColumnType<>(SQLPrimitives.STRING, JsonElement::toString, JsonParser::parseString);
    public static final ColumnType<NbtElement> NBT = new ColumnType<>(SQLPrimitives.STRING, NbtElement::toString, v -> {
        try {
            return StringNbtReader.parse(v);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    });
    public static final ColumnType<Text> TEXT = new ColumnType<>(SQLPrimitives.STRING, TextParser::textToString, TextParser::stringToText);
    public static final ColumnType<Identifier> IDENTIFIER = new ColumnType<>(SQLPrimitives.STRING, Identifier::toString, Identifier::tryParse);

    // Adventure Data Types
    public static final ColumnType<Key> ADVENTURE_KEY = new ColumnType<>(SQLPrimitives.STRING, Key::asMinimalString, Key::key);
    public static final ColumnType<Component> ADVENTURE_COMPONENT = new ColumnType<>(SQLPrimitives.STRING, v -> MiniMessage.miniMessage().serialize(v), v -> MiniMessage.miniMessage().deserialize(v));
}

