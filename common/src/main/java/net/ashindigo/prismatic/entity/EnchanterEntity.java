package net.ashindigo.prismatic.entity;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EnchanterEntity extends BlockEntity implements ExtendedMenuProvider {

    public EnchanterEntity(BlockPos blockPos, BlockState blockState) {
        super(PrismaticEnchanterMod.ENCHANTER_ENTITY.getOrNull(), blockPos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.prismatic_enchanter.enchanter");
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {

    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new EnchanterMenu(i, inventory);
    }
}
