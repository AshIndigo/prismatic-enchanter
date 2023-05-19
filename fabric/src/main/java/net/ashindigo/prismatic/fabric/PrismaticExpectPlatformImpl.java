package net.ashindigo.prismatic.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PrismaticExpectPlatformImpl {

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
