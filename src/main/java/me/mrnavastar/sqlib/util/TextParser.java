package me.mrnavastar.sqlib.util;

import com.google.gson.JsonElement;
import me.mrnavastar.sqlib.SQLib;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TextParser {

    private static Method serialize = null;
    private static Method deserialize = null;

    static {
        // 1.20.3 & up
        try {
            Class<?> clazz = Class.forName("net.minecraft.text.Text$Serialization");
            serialize = clazz.getMethod("toJsonString", Text.class);
            deserialize = clazz.getMethod("fromJsonTree", JsonElement.class);
        } catch (NoSuchMethodException | ClassNotFoundException ignore) {}

        // 1.20.2 & down
        try {
            Class<?> clazz = Class.forName("net.minecraft.text.Text$Serializer");
            serialize = clazz.getMethod("toJson", Text.class);
            deserialize = clazz.getMethod("fromJson", JsonElement.class);
        } catch (NoSuchMethodException | ClassNotFoundException ignore) {}
    }

    public static String textToString(Text text) {
        try {
            return (String) serialize.invoke(null, text);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Text stringToText(String s) throws InvocationTargetException, IllegalAccessException {
        return (Text) deserialize.invoke(null, SQLib.GSON.fromJson(s, JsonElement.class));
    }
}