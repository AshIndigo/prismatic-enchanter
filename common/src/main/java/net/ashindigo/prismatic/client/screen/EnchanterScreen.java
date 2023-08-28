package net.ashindigo.prismatic.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EnchanterScreen extends AbstractContainerScreen<EnchanterMenu> {

    private static final ResourceLocation ENCHANTING_TABLE_LOCATION = PrismaticEnchanterMod.makeResourceLocation("textures/gui/enchanter.png");
    public static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    public EditBox searchBox;
    public float scrollOffs;
    public boolean scrolling;

    public List<Enchantment> enchantmentList = new ArrayList<>();

    public EnchanterScreen(EnchanterMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        searchBox = new EditBox(this.font, this.leftPos + 52, this.topPos + 6, 110, 8, Component.translatable("text.search")); // 82

        addRenderableWidget(searchBox);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float rT, int x, int y) {
        this.renderBackground(poseStack);
        Lighting.setupForFlatItems();
        RenderSystem.setShaderTexture(0, ENCHANTING_TABLE_LOCATION);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        EnchanterScreen.blit(poseStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        RenderSystem.setShaderTexture(0, CREATIVE_INVENTORY_TABS);
        int i = this.leftPos + 175;
        int j = this.topPos + 18;
        int kK = j + 112;
        blit(poseStack, i, j + (int) ((float) (kK - j - 17) * this.scrollOffs), 232, 0, 12, 15);
    }

    // Searching

    @Override
    public boolean charTyped(char c, int i) {
        String s = this.searchBox.getValue();
        if (this.searchBox.charTyped(c, i)) {
            if (!Objects.equals(s, this.searchBox.getValue())) {
                this.refreshSearchResults();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
//        boolean flag = !this.isCreativeSlot(this.hoveredSlot) || this.hoveredSlot.hasItem();
//        boolean flag1 = InputConstants.getKey(i, j).getNumericKeyValue().isPresent();
//        if (flag && flag1 && this.checkHotbarKeyPressed(i, j)) {
//            //this.ignoreTextInput = true;
//            return true;
//        }
        String s = this.searchBox.getValue();
        if (this.searchBox.keyPressed(i, j, k)) {
            if (!Objects.equals(s, this.searchBox.getValue())) {
                this.refreshSearchResults();
            }
            return true;
        }
        return this.searchBox.isFocused() && this.searchBox.isVisible() && i != 256 ? true : super.keyPressed(i, j, k);
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        //this.ignoreTextInput = false;
        return super.keyReleased(i, j, k);
    }

    private void refreshSearchResults() {
        enchantmentList.clear();
//        this.visibleTags.clear();
        String s = this.searchBox.getValue();
        if (s.isEmpty()) {
            // All enchantments
            BuiltInRegistries.ENCHANTMENT.entrySet().forEach(key -> enchantmentList.add(key.getValue()));
            //(this.menu).items.addAll(selectedTab.getDisplayItems());
        } else {
            BuiltInRegistries.ENCHANTMENT.entrySet().stream().filter(resourceKeyEnchantmentEntry -> resourceKeyEnchantmentEntry.getValue().getFullname(1).getString().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))).forEachOrdered(resourceKeyEnchantmentEntry -> enchantmentList.add(resourceKeyEnchantmentEntry.getValue()));
        }
        this.scrollOffs = 0.0f;
        this.scrollTo(0.0f);
    }


    // Scrolling

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            if (this.insideScrollbar(pMouseX, pMouseY)) {
                this.scrolling = this.menu.slots.size() > 90;
                return true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            this.scrolling = false;
        }
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
//        if (menu.slots.size() - 36 == 0) return true;
//        int i = (menu.slots.size() - 36 + StorageCabinetBlock.getWidth() - 1) / StorageCabinetBlock.getWidth() - selectedHeight.getVerticalSlotCount();
        if (3 - 4 == 0) return true; // todo
        int i = 1; // TODO
        setScrollOffs((float) ((double) this.scrollOffs - pDelta / (double) i));
        scrollMenu(scrollOffs);
        return true;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling) {
            int i = this.topPos + 18;
            int j = i + 112;
            setScrollOffs(((float) pMouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F));
            scrollMenu(scrollOffs);
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    protected boolean insideScrollbar(double x, double y) {
        int i = this.leftPos;
        int j = this.topPos;
        int k = i + 175;
        int l = j + 18;
        int i1 = k + 14;
        int j1 = l + 112;
        return x >= (double) k && y >= (double) l && x < (double) i1 && y < (double) j1;
    }

    protected void setScrollOffs(float val) {
        this.scrollOffs = Mth.clamp(val, 0.0F, 1.0F);
    }

    public void scrollMenu(float pos) {
        // Fuck me, gotta scroll through the list of existing enchants...

    }

    public void scrollTo(float pos) {
//        int i = (this.entity.getContainerSize() + 9 - 1) / 9 - getDisplayHeight().getVerticalSlotCount(); // 25.8888888889 for 270 slots
//        int j = (int) ((double) (pos * (float) i) + 0.5D);
//
//        if (j < 0) {
//            j = 0;
//        }
//
//        // Iterate through all slots
//        for (int y = 0; y < StorageCabinetBlock.getHeight(tier); ++y) {
//            for (int x = 0; x < StorageCabinetBlock.getWidth(); ++x) {
//                if (j == 0) {
//                    StorageCabinetExpectPlatform.setSlotY(slots.get(y * 9 + x), 18 + y * 18);
//                } else {
//                    StorageCabinetExpectPlatform.setSlotY(slots.get(y * 9 + x), 18 + (y - j) * 18);
//                }
//
//            }
//        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        searchBox.tick();
    }
}
