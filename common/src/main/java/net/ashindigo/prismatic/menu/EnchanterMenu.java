package net.ashindigo.prismatic.menu;

import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.entity.EnchanterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EnchanterMenu extends AbstractContainerMenu {

    public EnchanterEntity entity;

    public EnchanterMenu(int i, Inventory inv, BlockPos pos) {
        super(PrismaticEnchanterMod.ENCHANTER_MENU.get(), i);
        entity = (EnchanterEntity) inv.player.level.getBlockEntity(pos);
        int j;
        this.addSlot(new Slot(entity, 0, 15, 47) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return (stack.getItem().getMaxStackSize() == 1 && stack.getItem().canBeDepleted()) || stack.getItem() == Items.BOOK;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inv, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }
        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(inv, j, 8 + j * 18, 142));
        }
    }

    public EnchanterMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        this(i, inventory, buf.readBlockPos());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(itemStack2, 2, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.slots.get(0).hasItem() && this.slots.get(0).mayPlace(itemStack2)) {
                ItemStack itemStack3 = itemStack2.copy();
                itemStack3.setCount(1);
                itemStack2.shrink(1);
                this.slots.get(0).setByPlayer(itemStack3);
            } else {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
    }
    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
