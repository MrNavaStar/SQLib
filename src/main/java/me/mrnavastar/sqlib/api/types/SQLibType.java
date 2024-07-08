package me.mrnavastar.sqlib.api.types;

import lombok.Getter;
import me.mrnavastar.sqlib.impl.SQLPrimitive;

import java.util.function.Function;

public class SQLibType<T> {
    @Getter
    private final SQLPrimitive<?> type;
    private final Function<T, Object> serializer;
    private final Function<Object, T> deserializer;

    public <S> SQLibType(SQLPrimitive<S> type, Function<T, S> serializer, Function<S, T> deserializer) {
        this.type = type;
        this.serializer = (Function<T, Object>) serializer;
        this.deserializer = (Function<Object, T>) deserializer;
    }

    public <S> SQLibType(SQLibType<S> type, Function<T, S> serializer, Function<S, T> deserializer) {
        this.type = type.getType();
        this.serializer = serializer.andThen(type.serializer);
        this.deserializer = type.deserializer.andThen(deserializer);
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
}