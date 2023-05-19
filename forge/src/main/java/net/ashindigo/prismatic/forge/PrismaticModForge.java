package net.ashindigo.prismatic.forge;

import dev.architectury.platform.forge.EventBuses;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PrismaticEnchanterMod.MOD_ID)
public class PrismaticModForge {
    public PrismaticModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PrismaticEnchanterMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        PrismaticEnchanterMod.init();
    }
}
