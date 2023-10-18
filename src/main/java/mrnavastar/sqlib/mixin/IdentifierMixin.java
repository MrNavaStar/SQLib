package mrnavastar.sqlib.mixin;

import mrnavastar.sqlib.api.MojangDataType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Identifier.class)
public abstract class IdentifierMixin implements MojangDataType {

    @Shadow public abstract String toString();

    @Override
    public String SQLib$encode() {
        return toString();
    }
}
