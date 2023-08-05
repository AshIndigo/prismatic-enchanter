package net.ashindigo.prismatic.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class EnchanterScreen extends AbstractContainerScreen<EnchanterMenu> {

    public EnchanterScreen(EnchanterMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {

    }
}
