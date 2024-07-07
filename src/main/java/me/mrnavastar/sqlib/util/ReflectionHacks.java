package me.mrnavastar.sqlib.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class ReflectionHacks {

    private static Unsafe unsafe;

    static{
        try{
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void setFinalStatic(Field field, Object value) {
        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);
        unsafe.putObject(fieldBase, fieldOffset, value);
    }
}
