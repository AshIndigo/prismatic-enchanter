package net.ashindigo.prismatic.forge.datagen;

import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PrismaticBlockStateModelProvider extends BlockStateProvider {
    private ExistingFileHelper helper;

    public PrismaticBlockStateModelProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator.getPackOutput(), PrismaticEnchanterMod.MOD_ID, helper);
        this.helper = helper;
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
