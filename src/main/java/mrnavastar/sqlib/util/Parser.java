package mrnavastar.sqlib.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.math.BlockPos;

public class Parser {

    public static NbtCompound nbtFromString(String nbt) {
        try {
            return StringNbtReader.parse(nbt);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BlockPos blockPosFromString(String pos) {
        String[] values = pos.split(", ");
        return new BlockPos(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
    }

    public static String[] stringArrayFromString(String string) {
        return string.replaceAll("[{}]", "").split(", ");
    }
}
