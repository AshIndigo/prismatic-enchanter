package net.ashindigo.prismatic;

import com.google.common.base.Suppliers;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.ashindigo.prismatic.block.EnchanterBlock;
import net.ashindigo.prismatic.entity.EnchanterEntity;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
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
    public static final RegistrySupplier<Block> ENCHANTER = BLOCKS.register("enchanter", () -> new EnchanterBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_BLACK)));

    public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final RegistrySupplier<BlockEntityType<?>> ENCHANTER_ENTITY = ENTITIES.register("enchanter", () -> BlockEntityType.Builder.of(EnchanterEntity::new).build(null));

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(MOD_ID, Registries.MENU);
    public static final RegistrySupplier<MenuType<EnchanterMenu>> ENCHANTER_MENU = MENUS.register("enchanter", () -> MenuRegistry.ofExtended(EnchanterMenu::new));


    public static void init() {
        ITEMS.register();
        BLOCKS.register();
        ENTITIES.register();
        MENUS.register();

        System.out.println(PrismaticExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());

        ClientLifecycleEvent.CLIENT_SETUP.register((mc) -> PrismaticEnchanterModClient.clientInit());
    }
}
