package net.ashindigo.prismatic.forge.datagen;

import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
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
                .define('D', Blocks.EMERALD_BLOCK)
                .pattern("EEE").pattern(" D ")
                .unlockedBy("has_emerald_block", VanillaRecipeProvider.has(Blocks.EMERALD_BLOCK))
                .save(consumer);
    }
}
