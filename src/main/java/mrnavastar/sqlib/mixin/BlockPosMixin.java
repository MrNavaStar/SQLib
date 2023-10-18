package mrnavastar.sqlib.mixin;

import mrnavastar.sqlib.api.MojangDataType;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockPos.class)
public abstract class BlockPosMixin implements MojangDataType {

    @Shadow public abstract long asLong();

    @Override
    public String SQLib$encode() {
        return String.valueOf(asLong());
    }

    private static void test() {

    }
}