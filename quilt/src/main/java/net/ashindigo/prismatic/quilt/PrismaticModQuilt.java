package net.ashindigo.prismatic.quilt;

import net.ashindigo.prismatic.fabriclike.PrismaticModFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class PrismaticModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        PrismaticModFabricLike.init();
    }
}
