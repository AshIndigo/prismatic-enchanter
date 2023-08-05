package net.ashindigo.prismatic.entity;

import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EnchanterEntity extends BlockEntity {

    public EnchanterEntity(BlockPos blockPos, BlockState blockState) {
        super(PrismaticEnchanterMod.ENCHANTER_ENTITY.getOrNull(), blockPos, blockState);
    }
}
