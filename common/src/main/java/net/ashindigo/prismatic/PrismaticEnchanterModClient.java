package net.ashindigo.prismatic;

import dev.architectury.registry.menu.MenuRegistry;
import net.ashindigo.prismatic.client.screen.EnchanterScreen;
import net.ashindigo.prismatic.mixin.MixinMinecraft;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.FullTextSearchTree;
import net.minecraft.client.searchtree.IdSearchTree;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PrismaticEnchanterModClient {

    public static void clientInit() {
        MenuRegistry.registerScreenFactory(PrismaticEnchanterMod.ENCHANTER_MENU.get(), EnchanterScreen::new);
        // TODO It needs to handle every level somehow...
//        ((MixinMinecraft) Minecraft.getInstance()).getSearchRegistry().register(PrismaticEnchanterMod.ENCHANTMENT_KEY, list -> new IdSearchTree<>(enchantment -> BuiltInRegistries.ENCHANTMENT.keySet().stream(), list));
//
//        List<Enchantment> enchantmentList = new ArrayList<>();
//        BuiltInRegistries.ENCHANTMENT.entrySet().forEach(key -> enchantmentList.add(key.getValue()));
//        Minecraft.getInstance().populateSearchTree(PrismaticEnchanterMod.ENCHANTMENT_KEY, enchantmentList);
    }
}
