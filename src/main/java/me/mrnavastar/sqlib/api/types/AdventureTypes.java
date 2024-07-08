package me.mrnavastar.sqlib.api.types;

import me.mrnavastar.sqlib.impl.SQLPrimitive;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class AdventureTypes {

    public static final SQLibType<Key> KEY = new SQLibType<>(SQLPrimitive.STRING, Key::asMinimalString, Key::key);
    public static final SQLibType<Component> COMPONENT = new SQLibType<>(SQLPrimitive.STRING, v -> MiniMessage.miniMessage().serialize(v), v -> MiniMessage.miniMessage().deserialize(v));
}