package net.ashindigo.prismatic.quilt;

import net.ashindigo.prismatic.fabriclike.PrismaticModFabricLikeClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class PrismaticModQuiltClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        PrismaticModFabricLikeClient.init();
    }
}
