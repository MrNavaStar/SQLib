package me.mrnavastar.sqlib.api.types;

import me.mrnavastar.sqlib.impl.SQLPrimitive;

import java.awt.*;
import java.util.Date;

public class JavaTypes {

    // Primitives
    public static final SQLibType<Byte> BYTE = new SQLibType<>(SQLPrimitive.INT, Byte::intValue, Integer::byteValue);
    public static final SQLibType<byte[]> BYTES = new SQLibType<>(SQLPrimitive.BYTES, v -> v, v -> v);
    public static final SQLibType<Boolean> BOOL = new SQLibType<>(SQLPrimitive.INT, v -> v ? 1 : 0, v -> v == 1);
    public static final SQLibType<Short> SHORT = new SQLibType<>(SQLPrimitive.SHORT, v -> v, v -> v);
    public static final SQLibType<Integer> INT = new SQLibType<>(SQLPrimitive.INT, v -> v, v -> v);
    public static final SQLibType<Float> FLOAT = new SQLibType<>(SQLPrimitive.FLOAT, v -> v, v -> v);
    public static final SQLibType<Double> DOUBLE = new SQLibType<>(SQLPrimitive.DOUBLE, v -> v, v -> v);
    public static final SQLibType<Long> LONG = new SQLibType<>(SQLPrimitive.LONG, v -> v, v -> v);
    public static final SQLibType<String> STRING = new SQLibType<>(SQLPrimitive.STRING, v -> v, v -> v);
    public static final SQLibType<Character> CHAR = new SQLibType<>(SQLPrimitive.CHAR, v -> v, v -> v);

    // Java Data Types
    public static final SQLibType<Date> DATE = new SQLibType<>(SQLPrimitive.LONG, Date::getTime, Date::new);
    public static final SQLibType<Color> COLOR = new SQLibType<>(SQLPrimitive.INT, Color::getRGB, Color::new);
    public static final SQLibType<java.util.UUID> UUID = new SQLibType<>(SQLPrimitive.STRING, java.util.UUID::toString, java.util.UUID::fromString);
}
