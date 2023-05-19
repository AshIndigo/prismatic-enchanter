package net.ashindigo.prismatic.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PrismaticExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
