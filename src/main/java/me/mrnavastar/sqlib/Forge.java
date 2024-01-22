package me.mrnavastar.sqlib;

import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod(SQLib.MOD_ID)
public class Forge extends SQLib {

    public Forge() {
        init(Path.of("./sqlib"), Path.of("./config"));
    }
}
