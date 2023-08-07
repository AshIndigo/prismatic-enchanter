package net.ashindigo.prismatic.forge;

import dev.architectury.platform.forge.EventBuses;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.forge.datagen.PrismaticBlockStateModelProvider;
import net.ashindigo.prismatic.forge.datagen.PrismaticItemModelProvider;
import net.ashindigo.prismatic.forge.datagen.PrismaticRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PrismaticEnchanterMod.MOD_ID)
public class PrismaticModForge {
    public PrismaticModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PrismaticEnchanterMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        PrismaticEnchanterMod.init();
    }

    @SubscribeEvent
    public void dataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new PrismaticRecipeProvider(generator));
        generator.addProvider(event.includeClient(), new PrismaticItemModelProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new PrismaticBlockStateModelProvider(generator, event.getExistingFileHelper()));
    }
}
