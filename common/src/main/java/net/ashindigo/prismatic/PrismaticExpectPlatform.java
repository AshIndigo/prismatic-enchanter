package net.ashindigo.prismatic;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class PrismaticExpectPlatform {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
