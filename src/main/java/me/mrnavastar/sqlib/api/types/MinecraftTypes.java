package me.mrnavastar.sqlib.api.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.mrnavastar.sqlib.impl.SQLPrimitive;
import me.mrnavastar.sqlib.impl.TextParser;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;

public class MinecraftTypes {

    public static final SQLibType<Vec3i> VEC3I = new SQLibType<>(SQLPrimitive.LONG, v -> BlockPos.asLong(v.getX(), v.getY(), v.getZ()), v -> new Vec3i(BlockPos.unpackLongX(v), BlockPos.unpackLongY(v), BlockPos.unpackLongZ(v)));
    public static final SQLibType<BlockPos> BLOCKPOS = new SQLibType<>(SQLPrimitive.LONG, BlockPos::asLong, BlockPos::fromLong);
    public static final SQLibType<ChunkPos> CHUNKPOS = new SQLibType<>(SQLPrimitive.LONG, ChunkPos::toLong, ChunkPos::new);

    public static final SQLibType<Text> TEXT = new SQLibType<>(SQLPrimitive.STRING, TextParser::textToString, TextParser::stringToText);
    public static final SQLibType<Identifier> IDENTIFIER = new SQLibType<>(SQLPrimitive.STRING, Identifier::toString, Identifier::tryParse);
    public static final SQLibType<SoundEvent> SOUND = new SQLibType<>(IDENTIFIER, SoundEvent::getId, SoundEvent::of);

    public static final SQLibType<JsonElement> JSON = new SQLibType<>(SQLPrimitive.STRING, JsonElement::toString, JsonParser::parseString);
    public static final SQLibType<NbtElement> NBT = new SQLibType<>(SQLPrimitive.STRING, NbtElement::toString, v -> {
        try {
            return StringNbtReader.parse(v);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    });
}
