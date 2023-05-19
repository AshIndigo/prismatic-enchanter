package net.ashindigo.prismatic.fabric;

import org.quiltmc.loader.api.QuiltLoader;

import java.nio.file.Path;

public class PrismaticExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return QuiltLoader.getConfigDir();
    }
}
