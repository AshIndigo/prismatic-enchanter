package net.ashindigo.prismatic.forge.datagen;

import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PrismaticBlockStateModelProvider extends BlockStateProvider {
    private ExistingFileHelper helper;

    public PrismaticBlockStateModelProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator.getPackOutput(), PrismaticEnchanterMod.MOD_ID, helper);
        this.helper = helper;
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(PrismaticEnchanterMod.ENCHANTER.get());
        simpleBlockItem(PrismaticEnchanterMod.ENCHANTER.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(PrismaticEnchanterMod.MOD_ID, "block/enchanter")));
    }
}
