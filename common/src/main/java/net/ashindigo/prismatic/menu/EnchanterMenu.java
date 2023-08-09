package net.ashindigo.prismatic.menu;

import dev.architectury.fluid.FluidStack;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

public class EnchanterMenu extends AbstractContainerMenu {

    public EnchanterMenu(int i) {
        super(PrismaticEnchanterMod.ENCHANTER_MENU.getOrNull(), i);
    }

    public EnchanterMenu(int i, Inventory inv) {
        this(i);
    }

    public EnchanterMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        this(i);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {

        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
