package me.mrnavastar.sqlib.impl;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteParser {

    public static byte[] primToBytes(Object object) {
        if (object instanceof Byte) return new byte[]{(Byte) object};
        if (object instanceof byte[] by) return by;
        if (object instanceof Boolean b) return b ? new byte[1] : new byte[0];
        if (object instanceof Short s) return ByteBuffer.allocate(Short.BYTES).putShort(s).array();
        if (object instanceof Integer i) return ByteBuffer.allocate(Integer.BYTES).putInt(i).array();
        if (object instanceof Float f) return ByteBuffer.allocate(Float.BYTES).putFloat(f).array();
        if (object instanceof Double d) return ByteBuffer.allocate(Double.BYTES).putDouble(d).array();
        if (object instanceof Long l) return ByteBuffer.allocate(Long.BYTES).putLong(l).array();
        if (object instanceof Character c) return ByteBuffer.allocate(Character.BYTES).putInt(c).array();
        if (object instanceof String s) return ByteBuffer.allocate(s.length() + 1).putInt(s.length()).put(s.getBytes(StandardCharsets.UTF_8)).array();
        return null;
    }

    public static <T> T bytesToPrim(byte[] bytes, Class<T> clazz) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        if (clazz == Byte.class) return clazz.cast(bytes[0]);
        if (clazz == byte[].class) return clazz.cast(bytes);
        if (clazz == Boolean.class) return clazz.cast(bytes.length > 0);
        if (clazz == Short.class) return clazz.cast(buffer.getShort());
        if (clazz == Integer.class) return clazz.cast(buffer.getInt());
        if (clazz == Float.class) return clazz.cast(buffer.getFloat());
        if (clazz == Double.class) return clazz.cast(buffer.getDouble());
        if (clazz == Long.class) return clazz.cast(buffer.getLong());
        if (clazz == Character.class) return clazz.cast(buffer.getChar());
        if (clazz == String.class) {
            byte[] stringBytes = new byte[buffer.getInt()];
            buffer.get(stringBytes);
            return clazz.cast(new String(stringBytes, StandardCharsets.UTF_8));
        }
        return null;
    }
}
