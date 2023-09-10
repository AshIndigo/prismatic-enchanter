package net.ashindigo.prismatic;

import com.google.common.base.Suppliers;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.ashindigo.prismatic.block.EnchanterBlock;
import net.ashindigo.prismatic.entity.EnchanterEntity;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.ashindigo.prismatic.networking.EnchantPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.List;
import java.util.function.Supplier;

public class PrismaticEnchanterMod {
    public static final String MOD_ID = "prismatic";

    // Registering a new creative tab
    public static final CreativeTabRegistry.TabSupplier TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "prism_tab"), () -> new ItemStack(PrismaticEnchanterMod.ENCHANTER_ITEM.get()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);
    public static final RegistrySupplier<Block> ENCHANTER = BLOCKS.register("enchanter", () -> new EnchanterBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_BLACK)));
    public static final RegistrySupplier<Item> ENCHANTER_ITEM = ITEMS.register("enchanter", () -> new BlockItem(ENCHANTER.get(), new Item.Properties().arch$tab(PrismaticEnchanterMod.TAB)));


    public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final RegistrySupplier<BlockEntityType<?>> ENCHANTER_ENTITY = ENTITIES.register("enchanter", () -> BlockEntityType.Builder.of(EnchanterEntity::new).build(null));

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(MOD_ID, Registries.MENU);
    public static final RegistrySupplier<MenuType<EnchanterMenu>> ENCHANTER_MENU = MENUS.register("enchanter", () -> MenuRegistry.ofExtended(EnchanterMenu::new));

    public static final SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(MOD_ID);
    public static final MessageType ENCHANT_PACKET = NETWORK_MANAGER.registerC2S("enchant", EnchantPacket::new);


    public static void init() {
        BLOCKS.register();
        ITEMS.register();
        ENTITIES.register();
        MENUS.register();
    }

    public static ResourceLocation makeResourceLocation(String suffix) {
        return new ResourceLocation(MOD_ID, suffix);
    }

    public static int getTotalCost(List<EnchantmentInstance> selected) {
        int cost = 0;
        for (EnchantmentInstance enchantmentInstance : selected) {
            cost += (enchantmentInstance.enchantment.getRarity().ordinal() + 1) * 2;
        }
        return cost;
        //return selected.stream().mapToInt(inst -> inst.enchantment.getMinCost(inst.level)).sum();
    }
}
