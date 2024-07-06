package me.mrnavastar.sqlib.sql;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLPrimitives<T> {

    public enum Type {
        BYTES, BYTE, BOOL, SHORT, INT, FLOAT, DOUBLE, LONG, STRING, CHAR, DATE, DATETIME, TIME, TIMESTAMP
    }

    private final Type type;
    private final Class<T> clazz;

    //public static final SQLPrimitives<Byte> BYTE = new SQLPrimitives<>(Type.BYTE);
    public static final SQLPrimitives<byte[]> BYTES = new SQLPrimitives<>(Type.BYTES, byte[].class);
    public static final SQLPrimitives<Short> SHORT = new SQLPrimitives<>(Type.SHORT, Short.class);
    public static final SQLPrimitives<Integer> INT = new SQLPrimitives<>(Type.INT, Integer.class);
    public static final SQLPrimitives<Float> FLOAT = new SQLPrimitives<>(Type.FLOAT, Float.class);
    public static final SQLPrimitives<Double> DOUBLE = new SQLPrimitives<>(Type.DOUBLE, Double.class);
    public static final SQLPrimitives<Long> LONG = new SQLPrimitives<>(Type.LONG, Long.class);
    public static final SQLPrimitives<Character> CHAR = new SQLPrimitives<>(Type.CHAR, Character.class);
    public static final SQLPrimitives<String> STRING = new SQLPrimitives<>(Type.STRING, String.class);

    public static final SQLPrimitives<Date> DATE = new SQLPrimitives<>(Type.DATE, Date.class);
    public static final SQLPrimitives<LocalDateTime> DATETIME = new SQLPrimitives<>(Type.DATETIME, LocalDateTime.class);
    public static final SQLPrimitives<Time> TIME = new SQLPrimitives<>(Type.TIME, Time.class);
    public static final SQLPrimitives<Timestamp> TIMESTAMP = new SQLPrimitives<>(Type.TIMESTAMP, Timestamp.class);
}
