package net.ashindigo.prismatic.fabric;

import net.ashindigo.prismatic.fabriclike.PrismaticModFabricLike;
import net.fabricmc.api.ModInitializer;

public class PrismaticModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PrismaticModFabricLike.init();
    }
}
