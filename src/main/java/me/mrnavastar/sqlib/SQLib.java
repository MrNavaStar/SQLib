package me.mrnavastar.sqlib;

import me.mrnavastar.sqlib.api.database.Database;

import java.util.List;

public class SQLib {

    protected static Database database;

    public static Database getDatabase() {
        if (database != null) return database;

        try {
            Class.forName("net.fabricmc.loader.api.FabricLoader");
            Fabric.init();
            return database;
        } catch (ClassNotFoundException ignore) {}

        try {
            Class.forName("org.quiltmc.loader.api.QuiltLoader");
            Quilt.init();
            return database;
        } catch (ClassNotFoundException ignore) {}

        try {
            Class.forName("com.velocitypowered.api.plugin.Plugin");
            Velocity.init();
            return database;
        } catch (ClassNotFoundException ignore) {
            throw new RuntimeException("SQLib currently only support Fabric, Quilt, and Velocity!");
        }
    }

    public static List<Database> getDatabases() {
        return Database.getDatabases();
    }
}