package net.ashindigo.prismatic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EnchantmentListComponent extends GuiComponent implements Renderable, GuiEventListener, NarratableEntry {

    protected static final ResourceLocation RECIPE_BOOK_LOCATION = PrismaticEnchanterMod.makeResourceLocation("textures/gui/enchanter.png");
    public static final int IMAGE_WIDTH = 135;
    public static final int IMAGE_HEIGHT = 166;
    private static final int OFFSET_X_POSITION = 142;
    private int xOffset;
    private int width;
    private int height;

    protected Minecraft minecraft;
    private boolean visible;
    private boolean widthTooNarrow;
    private EnchanterScreen enchanterScreen;
    private final ArrayList<SelectedEnchantmentButton> selectedEntryList = new ArrayList<>();

    public void init(int width, int height, Minecraft minecraft, boolean widthTooNarrow, EnchanterScreen enchanterScreen) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.widthTooNarrow = widthTooNarrow;
        this.enchanterScreen = enchanterScreen;
        setVisible(false);
        initVisuals();

    }

    public void initVisuals() {
        this.xOffset = this.widthTooNarrow ? 0 : OFFSET_X_POSITION;
    }

    public int updateScreenPosition(int width, int imageWidth) {
        if (this.isVisible() && !this.widthTooNarrow) {
            return 177 + (width - imageWidth - 200) / 2;
        }
        return (width - imageWidth) / 2;
    }

    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }

    public boolean isVisible() {
        return this.visible;
    }

    protected void setVisible(boolean visible) {
        if (visible) {
            this.initVisuals();
        }
        this.visible = visible;
        selectedEntryList.forEach(btn -> btn.setVisible(visible));
    }


    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (!this.isVisible()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 0);
        RenderSystem.setShaderTexture(0, RECIPE_BOOK_LOCATION);
        int rX = (this.width - IMAGE_WIDTH) / 2 - this.xOffset;
        int rY = (this.height - IMAGE_HEIGHT) / 2;
        blit(poseStack, rX, rY, 167, 166, IMAGE_WIDTH, IMAGE_HEIGHT, 512, 512);
        minecraft.font.draw(poseStack, Component.translatable("text.prismatic.review"), rX + 6, rY + 5, 0x404040);
        poseStack.popPose();
    }

    @Override
    public void setFocused(boolean focused) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public NarratableEntry.@NotNull NarrationPriority narrationPriority() {
        return this.visible ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        // TODO ?
    }

    public void updateList() {
        ArrayList<SelectedEnchantmentButton> backList = new ArrayList<>(selectedEntryList);
        clearList();
        backList.forEach(btn->addToList(btn.enchantmentInstance.enchantment, btn.enchantmentInstance.level));
    }

    public void removeFromList(EnchantmentInstance enchantmentInstance) {
        selectedEntryList.stream().filter(selectedEnchantmentButton -> selectedEnchantmentButton.enchantmentInstance.enchantment == enchantmentInstance.enchantment).findFirst().get().removeButton();
        selectedEntryList.removeIf(selectedEnchantmentButton -> selectedEnchantmentButton.enchantmentInstance.enchantment == enchantmentInstance.enchantment);
        enchanterScreen.selected.removeIf(inst -> inst.enchantment == enchantmentInstance.enchantment);
        enchanterScreen.enchantCheck();
        updateList();
    }

    public void addToList(Enchantment entry, int level) {
        int i = selectedEntryList.size();
        EnchantmentInstance inst = new EnchantmentInstance(entry, level);
        SelectedEnchantmentButton btn = enchanterScreen.addRenderableWidget(new SelectedEnchantmentButton(updateScreenPosition(enchanterScreen.width, enchanterScreen.getImageWidth()) - 111, enchanterScreen.getTopPos() + 25 + (i * 19), 90, 19, entry.getFullname(level), button -> removeFromList(inst), (supplier) -> entry.getFullname(level).copy(), inst, isVisible()));
        selectedEntryList.add(btn);
    }

    public void clearList() {
        selectedEntryList.forEach(SelectedEnchantmentButton::removeButton);
        selectedEntryList.clear();
    }


    public class SelectedEnchantmentButton extends Button {

        final Button remove;
        private final EnchantmentInstance enchantmentInstance;

        public SelectedEnchantmentButton(int i, int j, int k, int l, Component component, OnPress onPress, CreateNarration createNarration, EnchantmentInstance enchantmentInstance, boolean visible) {
            super(i, j, k, l, component, button -> {}, createNarration);
            this.enchantmentInstance = enchantmentInstance;
            remove = new Button(i - 20, j, 20, 19, Component.literal("X"), onPress, createNarration) {
                @Override
                public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                    poseStack.pushPose();
                    poseStack.translate(0,0, 5); // This is stupid
                    super.render(poseStack, mouseX, mouseY, partialTick);
                    poseStack.popPose();
                }
            };
            enchanterScreen.addRenderableWidget(remove);
            this.setVisible(visible);
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            if ((this.getY() < (enchanterScreen.getTopPos() + 24) + (7 * 19)) && getY() > (enchanterScreen.getTopPos() + 24)) {
                if (visible) {
                    poseStack.pushPose();
                    poseStack.translate(0,0, 5);
                    super.renderWidget(poseStack, mouseX, mouseY, partialTick);
                    poseStack.popPose();
                }
            }
        }

        @Override
        public void setPosition(int x, int y) {
            super.setPosition(x, y);
            remove.setPosition(x, y);
        }

        @Override
        public void setX(int x) {
            super.setX(x);
            remove.setX(x - 10);
        }

        @Override
        public void setY(int y) {
            super.setY(y);
            remove.setY(y);
            if (visible) {
                remove.visible = (this.getY() < (enchanterScreen.getTopPos() + 24) + (7 * 19)) && getY() > (enchanterScreen.getTopPos() + 24);
            }
        }

        public void setVisible(boolean vis) {
            visible = vis;
            remove.visible = vis;
        }

        public void removeButton() {
            enchanterScreen.removeWidget(remove);
            enchanterScreen.removeWidget(this);
        }
    }
}
