package net.ashindigo.prismatic.forge.datagen;

import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class PrismaticRecipeProvider extends RecipeProvider {

    public PrismaticRecipeProvider(DataGenerator generator) {
        super(generator.getPackOutput());
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PrismaticEnchanterMod.ENCHANTER.get())
                .define('E', Blocks.ENCHANTING_TABLE)
                .define('D', Items.EMERALD)
                .define('O', Blocks.OBSIDIAN)
                .define('W', Blocks.WITHER_SKELETON_SKULL)
                .pattern("DWD").pattern("OEO")
                .unlockedBy("has_obsidian", VanillaRecipeProvider.has(Blocks.OBSIDIAN))
                .save(consumer);
    }
}
