package me.mrnavastar.sqlib.impl;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

// This class is used for backwards compatibility as 1.21.2 changed SoundEvent to a record
public class SoundParser {

    private static Field id = null;

    static {
        try {
            id = SoundEvent.class.getDeclaredField("comp_3319");
        } catch (NoSuchFieldException ignore) {}

        // DEV
        try {
            id = SoundEvent.class.getDeclaredField("id");
        } catch (NoSuchFieldException ignore) {}

        id.setAccessible(true);
    }

    public static Identifier getId(SoundEvent event) {
        try {
            return (Identifier) id.get(event);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
