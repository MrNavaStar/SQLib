package me.mrnavastar.sqlib;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import java.nio.file.Path;

@Plugin(
        id = "sqlib",
        name = "SQLib",
        version = "debug-build",
        authors = "MrNavaStar",
        description = "A simple SQL wrapper with a focus on Minecraft use cases"
)
public class Velocity extends SQLib {

    @Inject
    public Velocity(@DataDirectory Path dataDirectory) {
        init(dataDirectory, dataDirectory);
    }
}
