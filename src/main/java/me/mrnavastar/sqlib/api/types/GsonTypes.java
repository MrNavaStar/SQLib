package me.mrnavastar.sqlib.api.types;

import com.google.gson.*;
import me.mrnavastar.sqlib.impl.SQLPrimitive;

public class GsonTypes {
    public static final SQLibType<JsonElement> ELEMENT = new SQLibType<>(SQLPrimitive.STRING, JsonElement::toString, JsonParser::parseString);
    public static final SQLibType<JsonObject> OBJECT = new SQLibType<>(ELEMENT, v -> v, v -> (JsonObject) v);
    public static final SQLibType<JsonArray> ARRAY = new SQLibType<>(ELEMENT, v -> v, v -> (JsonArray) v);
    public static final SQLibType<JsonPrimitive> PRIMITIVE = new SQLibType<>(ELEMENT, v -> v, v -> (JsonPrimitive) v);
}
