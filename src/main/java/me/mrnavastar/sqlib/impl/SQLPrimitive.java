package me.mrnavastar.sqlib.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLPrimitive<T> {

    public enum Type {
        BYTES, BYTE, BOOL, SHORT, INT, FLOAT, DOUBLE, LONG, STRING, CHAR, DATE, DATETIME, TIME, TIMESTAMP
    }

    private final Type type;
    private final Class<T> clazz;

    public static final SQLPrimitive<byte[]> BYTES = new SQLPrimitive<>(Type.BYTES, byte[].class);
    public static final SQLPrimitive<Short> SHORT = new SQLPrimitive<>(Type.SHORT, Short.class);
    public static final SQLPrimitive<Integer> INT = new SQLPrimitive<>(Type.INT, Integer.class);
    public static final SQLPrimitive<Float> FLOAT = new SQLPrimitive<>(Type.FLOAT, Float.class);
    public static final SQLPrimitive<Double> DOUBLE = new SQLPrimitive<>(Type.DOUBLE, Double.class);
    public static final SQLPrimitive<Long> LONG = new SQLPrimitive<>(Type.LONG, Long.class);
    public static final SQLPrimitive<Character> CHAR = new SQLPrimitive<>(Type.CHAR, Character.class);
    public static final SQLPrimitive<String> STRING = new SQLPrimitive<>(Type.STRING, String.class);
}
