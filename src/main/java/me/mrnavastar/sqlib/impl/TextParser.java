package me.mrnavastar.sqlib.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@UtilityClass
public class TextParser {

    private static final Gson GSON = new Gson();
    private static Method serialize = null;
    private static Method deserialize = null;

    static {
        // 1.20.5 & up
        try {
            Class<?> clazz = Class.forName("net/minecraft/class_2561$class_2562");
            serialize = clazz.getMethod("method_10867", Text.class, RegistryWrapper.WrapperLookup.class);
            deserialize = clazz.getMethod("method_10872", JsonElement.class, RegistryWrapper.WrapperLookup.class);
        } catch (NoSuchMethodException | ClassNotFoundException ignore) {}

        // 1.20.3 & 1.20.4
        try {
            Class<?> clazz = Class.forName("net/minecraft/class_2561$class_2562");
            serialize = clazz.getMethod("method_10867", Text.class);
            deserialize = clazz.getMethod("method_10872", JsonElement.class);
        } catch (NoSuchMethodException | ClassNotFoundException ignore) {}

        // 1.20.2 & down
        try {
            Class<?> clazz = Class.forName("net/minecraft/class_2561$class_8822");
            serialize = clazz.getMethod("method_10867", Text.class);
            deserialize = clazz.getMethod("method_10872", JsonElement.class);
        } catch (NoSuchMethodException | ClassNotFoundException ignore) {}

        // DEV - 1.20.5 & up
        try {
            Class<?> clazz = Class.forName("net.minecraft.text.Text$Serialization");
            serialize = clazz.getDeclaredMethod("toJsonString", Text.class, RegistryWrapper.WrapperLookup.class);
            deserialize = clazz.getDeclaredMethod("fromJsonTree", JsonElement.class, RegistryWrapper.WrapperLookup.class);
        } catch (NoSuchMethodException | ClassNotFoundException ignore) {}

        // DEV - 1.20.3 & 1.20.4
        try {
            Class<?> clazz = Class.forName("net.minecraft.text.Text$Serialization");
            serialize = clazz.getDeclaredMethod("toJsonString", Text.class, RegistryWrapper.WrapperLookup.class);
            deserialize = clazz.getDeclaredMethod("fromJsonTree", JsonElement.class, RegistryWrapper.WrapperLookup.class);
        } catch (NoSuchMethodException | ClassNotFoundException ignore) {}

        // DEV - 1.20.2 & down
        try {
            Class<?> clazz = Class.forName("net.minecraft.text.Text$Serializer");
            serialize = clazz.getMethod("toJson", Text.class);
            deserialize = clazz.getMethod("fromJson", JsonElement.class);
        } catch (NoSuchMethodException | ClassNotFoundException ignore) {}
    }

    public static String textToString(Text text) {
        try {
            if (serialize.getParameterCount() > 1) {
                return (String) serialize.invoke(null, text, BuiltinRegistries.createWrapperLookup());
            }
            return (String) serialize.invoke(null, text);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static Text stringToText(String s) {
        if (serialize.getParameterCount() > 1) {
            return (Text) deserialize.invoke(null, GSON.fromJson(s, JsonElement.class), BuiltinRegistries.createWrapperLookup());
        }
        return (Text) deserialize.invoke(null, GSON.fromJson(s, JsonElement.class));
    }
}