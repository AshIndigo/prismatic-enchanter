package net.ashindigo.prismatic.forge.datagen;

import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PrismaticItemModelProvider extends ItemModelProvider {
    public PrismaticItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), PrismaticEnchanterMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(PrismaticEnchanterMod.ENCHANTER.getId().getPath(), modLoc("block/enchanter"));
    }
}
