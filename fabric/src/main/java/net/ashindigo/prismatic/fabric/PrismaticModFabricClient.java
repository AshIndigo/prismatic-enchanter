package net.ashindigo.prismatic.fabric;

import net.ashindigo.prismatic.fabriclike.PrismaticModFabricLikeClient;
import net.fabricmc.api.ClientModInitializer;

public class PrismaticModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PrismaticModFabricLikeClient.init();
    }
}
