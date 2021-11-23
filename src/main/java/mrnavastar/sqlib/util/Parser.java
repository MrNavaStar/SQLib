package mrnavastar.sqlib.util;

import com.google.common.primitives.Ints;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.UUID;

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
        return string.replace("]", "").replace("[", "").split(", ");
    }

    public static int[] intArrayFromString(String string) {
        String[] data = stringArrayFromString(string);
        ArrayList<Integer> ints = new ArrayList<>();
        for (String i : data) {
            ints.add(Integer.parseInt(i));
        }
        return Ints.toArray(ints);
    }

    public static UUID[] uuidArrayFromString(String string) {
        String[] data = stringArrayFromString(string);
        ArrayList<UUID> uuids = new ArrayList<>();
        for (String uuid : data) {
            uuids.add(UUID.fromString(uuid));
        }
        return uuids.toArray(new UUID[]{});
    }

    public static BlockPos[] blockPosArrayFromString(String string) {
        String[] data = stringArrayFromString(string);
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for (String pos : data) {
            blocks.add(blockPosFromString(pos));
        }
        return blocks.toArray(new BlockPos[]{});
    }
}