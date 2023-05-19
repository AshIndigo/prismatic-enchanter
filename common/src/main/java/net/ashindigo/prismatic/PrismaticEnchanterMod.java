package net.ashindigo.prismatic;

import com.google.common.base.Suppliers;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Supplier;

public class PrismaticEnchanterMod {
    public static final String MOD_ID = "prismatic_enchanter";

    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    // Registering a new creative tab
    public static final CreativeTabRegistry.TabSupplier TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "prism_tab"), () -> new ItemStack(PrismaticEnchanterMod.ENCHANTER_ITEM.get()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final RegistrySupplier<Item> ENCHANTER_ITEM = ITEMS.register("enchanter", () -> new Item(new Item.Properties().arch$tab(PrismaticEnchanterMod.TAB)));

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);
    public static final RegistrySupplier<Block> ENCHANTER = BLOCKS.register("enchanter", () -> new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_BLACK)));

    public static void init() {
        ITEMS.register();
        BLOCKS.register();

        System.out.println(PrismaticExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
