package net.ashindigo.prismatic.menu;

import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnchanterMenu extends AbstractContainerMenu {

    public EnchanterMenu(int i) {
        super(PrismaticEnchanterMod.ENCHANTER_MENU.getOrNull(), i);
    }

    public EnchanterMenu(int i, Inventory inv) {
        this(i);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
