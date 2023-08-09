package net.ashindigo.prismatic.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EnchanterScreen extends AbstractContainerScreen<EnchanterMenu> {

    private static final ResourceLocation ENCHANTING_TABLE_LOCATION = PrismaticEnchanterMod.makeResourceLocation("textures/gui/enchanter.png");
    public EditBox search;

    public EnchanterScreen(EnchanterMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        search = new EditBox(this.font, this.leftPos + 42, this.topPos + 6, 110, 8, Component.translatable("itemGroup.search")); // 82
        addRenderableWidget(search);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        this.renderBackground(poseStack);
        Lighting.setupForFlatItems();
        RenderSystem.setShaderTexture(0, ENCHANTING_TABLE_LOCATION);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        EnchanterScreen.blit(poseStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        search.tick();
    }
}
