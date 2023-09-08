package net.ashindigo.prismatic.entity;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.ashindigo.prismatic.BasicSidedInventory;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EnchanterEntity extends BlockEntity implements ExtendedMenuProvider, BasicSidedInventory {

    NonNullList<ItemStack> items = NonNullList.of(ItemStack.EMPTY, ItemStack.EMPTY);

    public EnchanterEntity(BlockPos blockPos, BlockState blockState) {
        super(PrismaticEnchanterMod.ENCHANTER_ENTITY.getOrNull(), blockPos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.prismatic.enchanter");
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, this.items);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new EnchanterMenu(i, inventory, getBlockPos());
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
