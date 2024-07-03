package me.mrnavastar.sqlib;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(
        id = "sqlib",
        name = "SQLib",
        version = "debug-build",
        authors = "MrNavaStar"
)
public non-sealed class Velocity extends SQLib {

    @Inject
    public Velocity(Logger logger, @DataDirectory Path dir) {
        init(dir, dir);
    }
}
