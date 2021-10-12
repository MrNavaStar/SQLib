package mrnavastar.sqlib.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.math.BlockPos;

public class Parser {

    public static NbtCompound nbtFromString(String nbt) {
        System.out.println("[SQLIB] " + nbt);
        try {
            return StringNbtReader.parse(nbt);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return new NbtCompound();
    }

    public static BlockPos blockPosFromString(String pos) {
        String cleaned = pos.substring(10, pos.length() - 1).replaceAll("[xyz= ]", "");
        String[] values = cleaned.split(",");
        return new BlockPos(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
    }
}
