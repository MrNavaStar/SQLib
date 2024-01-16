package me.mrnavastar.sqlib.util;

import com.google.gson.JsonElement;
import me.mrnavastar.sqlib.SQLib;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;

public class TextParser {

    private static Version v1_20_3 = null;
    private static Class Serialization = null;
    private static Class Serializer = null;

    static {
        try {
            v1_20_3 = Version.parse("1.20.3");
            Serialization = Class.forName("net.minecraft.text.Text.Serialization");
            Serializer = Class.forName("net.minecraft.text.Text.Serializer");
        } catch (ClassNotFoundException | VersionParsingException ignore) {}
    }

    public static String textToString(Text text) {
        try {
            if (SQLib.minecraftVersion.compareTo(v1_20_3) >= 0)
                return (String) Serialization.getMethod("toJsonString").invoke(text);
            else return (String) Serializer.getMethod("toJson").invoke(text);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Text stringToText(String s) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (SQLib.minecraftVersion.compareTo(v1_20_3) >= 0) return (Text) Serialization.getMethod("fromJsonTree").invoke(SQLib.GSON.fromJson(s, JsonElement.class));
        else return (Text) Serialization.getMethod("fromJson").invoke(s);
    }
}