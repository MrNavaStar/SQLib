package me.mrnavastar.sqlib.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteParser {

    public static byte[] primToBytes(Object object) {
        if (object instanceof byte[] by) return by;
        if (object instanceof String s) return ByteBuffer.allocate(s.length() + 1).putInt(s.length()).put(s.getBytes(StandardCharsets.UTF_8)).array();
        if (object instanceof Integer i) return ByteBuffer.allocate(Integer.BYTES).putInt(i).array();
        if (object instanceof Float f) return ByteBuffer.allocate(Float.BYTES).putFloat(f).array();
        if (object instanceof Double d) return ByteBuffer.allocate(Double.BYTES).putDouble(d).array();
        if (object instanceof Long l) return ByteBuffer.allocate(Long.BYTES).putLong(l).array();
        if (object instanceof Boolean b) return b ? new byte[1] : new byte[0];
        return null;
    }

    public static <T> T bytesToPrim(byte[] bytes, Class<T> clazz) {
        ByteBuffer buf = ByteBuffer.wrap(bytes);


        return (T) clazz;
    }
}
