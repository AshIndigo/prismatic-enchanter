package net.ashindigo.prismatic.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.ashindigo.prismatic.networking.EnchantPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class EnchanterScreen extends AbstractContainerScreen<EnchanterMenu> {

    /*
    actual block texture
    custom sprite for XP cost (and probably review)
    and a scrollbar for the review panel as well
     */

    private static final ResourceLocation ENCHANTING_TABLE_LOCATION = PrismaticEnchanterMod.makeResourceLocation("textures/gui/enchanter.png");
    public static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private final Inventory inventory;
    public EditBox searchBox;
    Button enchantBtn;
    StringWidget xpCost;
    public float scrollOffs;
    public boolean scrolling;

    public final List<Enchantment> enchantmentList = new ArrayList<>();
    public final List<EnchantmentButton> enchantmentEntryList = new ArrayList<>();
    public final List<EnchantmentInstance> selected = new ArrayList<>();

    public final EnchantmentListComponent enchantmentListComponent = new EnchantmentListComponent();

    public EnchanterScreen(EnchanterMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.inventory = inventory;
    }

    @Override
    protected void init() {
        super.init();
        menu.registerFunc(this::refreshSearchResults);
        this.imageHeight = 166;
        this.imageWidth = 302;
        this.leftPos = this.enchantmentListComponent.updateScreenPosition(this.width, this.imageWidth);
        //this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.enchantmentListComponent.init(this.width, this.height, this.minecraft, this.width < 379, this);
        searchBox = new EditBox(this.font, this.leftPos + 172, this.topPos + 14, 110, 10, Component.translatable("text.search")); // 82
        addRenderableWidget(searchBox);
        enchantBtn = Button.builder(Component.literal("Enchant"), btn -> {
            if (inventory.player.experienceLevel >= PrismaticEnchanterMod.getTotalCost(selected)) {
                new EnchantPacket(menu.entity.getBlockPos(), selected).sendToServer();
                Minecraft.getInstance().player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1, Minecraft.getInstance().level.random.nextFloat() * 0.1f + 0.9f);
                ItemStack stack = menu.getSlot(0).getItem();
                if (stack.is(Items.BOOK)) { // I'll take bad ideas for 5$
                    stack = Items.ENCHANTED_BOOK.getDefaultInstance();
                    for (EnchantmentInstance ench : selected) {
                        EnchantedBookItem.addEnchantment(stack, ench);
                    }
                } else {
                    for (EnchantmentInstance ench : selected) {
                        stack.enchant(ench.enchantment, ench.level);
                    }
                }
                clearSelected();
                refreshSearchResults(stack);
            }
        }).size(45, 18).pos(this.leftPos + 34, this.topPos + 46).build();
        addRenderableWidget(enchantBtn);
        Button clearBtn = Button.builder(Component.literal("Clear"), inst -> clearSelected()).size(30, 18).pos(this.leftPos + 132, this.topPos + 46).build();
        addRenderableWidget(clearBtn);

        xpCost = new StringWidget(Component.literal(Integer.toString(PrismaticEnchanterMod.getTotalCost(selected))), font);
        xpCost.setPosition(this.leftPos + 14 + 12, this.topPos + 21);
        addRenderableWidget(xpCost);
        ImageButton reviewButton = addRenderableWidget(new ImageButton(leftPos + 82, topPos + 46, 20, 18, 0, 0, 19, new ResourceLocation("textures/gui/recipe_button.png"), button -> {
            enchantmentListComponent.toggleVisibility();
            this.leftPos = this.enchantmentListComponent.updateScreenPosition(this.width, this.imageWidth);
            button.setX(this.leftPos + 82);
            enchantBtn.setX(this.leftPos + 34);
            clearBtn.setX(this.leftPos + 132);
            enchantmentEntryList.forEach(btn -> btn.setX(leftPos + 192));
            searchBox.setX(leftPos + 174);
            xpCost.setX(this.leftPos + 14 + 12);
        }));
        reviewButton.setTooltip(Tooltip.create(Component.translatable("text.prismatic.review")));
        addWidget(enchantmentListComponent);
        refreshSearchResults();
    }

    private void clearSelected() {
        selected.clear();
        enchantmentListComponent.clearList();
        updateCostText();
        enchantCheck();
        greyIncompatible(null);
    }

    private void updateCostText() {
        xpCost.setMessage(Component.literal(Integer.toString(PrismaticEnchanterMod.getTotalCost(selected))));
    }

    @Override
    protected void renderBg(PoseStack poseStack, float rT, int x, int y) {
        Lighting.setupForFlatItems();
        RenderSystem.setShaderTexture(0, ENCHANTING_TABLE_LOCATION);
        blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
        blit(poseStack, this.leftPos + 8, this.topPos + 18, 0, 223, 16, 16, 512, 512); // TODO Draws XP cost icon, needs custom graphic
        RenderSystem.setShaderTexture(0, CREATIVE_INVENTORY_TABS);
        int i = this.leftPos + 175;
        int j = this.topPos + 18 + 7;
        int kK = j + 112 + 7;
        blit(poseStack, i + 110, j + (int) ((float) (kK - j) * this.scrollOffs), 232, 0, 12, 15);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        partialTick = this.minecraft.getFrameTime();
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        enchantmentListComponent.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        if (slotId == 0) {
            refreshSearchResults();
        }
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
        String s = this.searchBox.getValue();
        if (this.searchBox.keyPressed(i, j, k)) {
            if (!Objects.equals(s, this.searchBox.getValue())) {
                this.refreshSearchResults();
            }
            return true;
        }
        return this.searchBox.isFocused() && this.searchBox.isVisible() && i != 256 || super.keyPressed(i, j, k);
    }

    public void refreshSearchResults() {
        refreshSearchResults(menu.getSlot(0).getItem());
    }

    public void refreshSearchResults(ItemStack stack) {
        enchantmentList.clear();
        enchantmentEntryList.forEach(EnchantmentButton::removeButton);
        enchantmentEntryList.clear();
        String s = this.searchBox.getValue();
        AtomicInteger i = new AtomicInteger();
        if (menu.getSlot(0).hasItem()) {
            getAvailableEnchantmentResults(BuiltInRegistries.ENCHANTMENT.entrySet().stream().filter(entry -> entry.getValue().getFullname(1).getString().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))), stack, true).forEach(inst -> {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                if (isCompatible(enchantments.keySet(), inst.enchantment)) {
                    if (enchantments.containsKey(inst.enchantment)) {
                        if (enchantments.get(inst.enchantment) != inst.enchantment.getMaxLevel()) {
                            addEnchantToList(inst.enchantment, i.getAndIncrement());
                        }
                    } else {
                        addEnchantToList(inst.enchantment, i.getAndIncrement());
                    }
                }
            });
        }
        enchantmentEntryList.forEach(this::addRenderableWidget);
        this.scrollOffs = 0.0f;
        this.scrollTo(0.0f);
    }

    public static List<EnchantmentInstance> getAvailableEnchantmentResults(Stream<Map.Entry<ResourceKey<Enchantment>, Enchantment>> ench, ItemStack stack, boolean allowTreasure) {
        ArrayList<EnchantmentInstance> list = Lists.newArrayList();
        Item item = stack.getItem();
        boolean bl = stack.is(Items.BOOK);
        block0:
        for (Map.Entry<ResourceKey<Enchantment>, Enchantment> res : ench.toList()) {
            Enchantment enchantment = res.getValue();
            if (enchantment.isTreasureOnly() && !allowTreasure || !enchantment.isDiscoverable() || !enchantment.category.canEnchant(item) && !bl)
                continue;
            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                list.add(new EnchantmentInstance(enchantment, i));
                continue block0; // Shhh
            }
        }
        return list;
    }

    public static boolean isCompatible(Collection<Enchantment> enchantments, Enchantment addition) {
        for (Enchantment e : enchantments) {
            if (e.equals(addition)) return true;
            if (!e.isCompatibleWith(addition)) {
                return false;
            }
        }
        return true;
    }

    private void addEnchantToList(Enchantment entry, int i) {
        enchantmentList.add(entry);
        enchantmentEntryList.add(new EnchantmentButton(leftPos + 173 + 20 - 1, topPos + 25 + (i * 19), entry.getFullname(1), button -> {
            if (selected.stream().anyMatch(inst -> inst.enchantment.equals(entry) && inst.level < ((EnchantmentButton) button).level)) {
                EnchantmentInstance enchantmentInstance = selected.stream().filter(inst -> inst.enchantment.equals(entry) && inst.level < ((EnchantmentButton) button).level).findFirst().get();
                selected.remove(enchantmentInstance);
                enchantmentListComponent.removeFromList(enchantmentInstance);
            }
            if (selected.stream().noneMatch(inst -> inst.enchantment.equals(entry)) && isCompatible(selected, entry)) {
                selected.add(new EnchantmentInstance(entry, ((EnchantmentButton) button).level));
                enchantmentListComponent.addToList(entry, ((EnchantmentButton) button).level);
                updateCostText();
                enchantCheck();
                greyIncompatible(entry);
            }
        }, (supplier) -> entry.getFullname(1).copy(), entry));
    }

    private void greyIncompatible(Enchantment entry) {
        if (entry == null) {
            this.enchantmentEntryList.forEach(btn -> btn.active = true);
            return;
        }
        this.enchantmentEntryList.forEach(btn -> btn.active = btn.enchant.isCompatibleWith(entry) || btn.enchant.equals(entry));
    }

    public static boolean isCompatible(List<EnchantmentInstance> selected, Enchantment entry) {
        ArrayList<Enchantment> enchants = new ArrayList<>();
        selected.forEach(sel -> enchants.add(sel.enchantment));
        return isCompatible(enchants, entry);
    }

    public void enchantCheck() {
        if ((minecraft.player.experienceLevel >= PrismaticEnchanterMod.getTotalCost(selected))) {
            ItemStack stack = menu.getSlot(0).getItem();
            for (EnchantmentInstance ench : selected) {
                if (EnchantmentHelper.getEnchantments(stack).containsKey(ench.enchantment)) {
                    if (EnchantmentHelper.getEnchantments(stack).get(ench.enchantment) < ench.level) {
                        enchantBtn.active = true;
                    } else {
                        enchantBtn.active = false;
                        return;
                    }
                } else {
                    enchantBtn.active = true;
                }
            }
        } else {
            enchantBtn.active = false;
        }
    }

    // Scrolling

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            if (this.insideScrollbar(pMouseX, pMouseY)) {
                this.scrolling = enchantmentList.size() > 7;
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
        if (enchantmentList.size() - 7 == 0) return true;
        int i = (enchantmentList.size() - 7) - 7;
        setScrollOffs((float) ((double) this.scrollOffs - pDelta / (double) i));
        scrollTo(scrollOffs);
        return true;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling) {
            int i = this.topPos + 25;
            int j = i + 112;
            setScrollOffs(((float) pMouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F));
            scrollTo(scrollOffs);
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    protected boolean insideScrollbar(double x, double y) {
        int i = this.leftPos;
        int j = this.topPos;
        int xCheck = i + 285;
        int yCheck = j + 25;
        int xCheckMax = xCheck + 14;
        int yCheckMax = yCheck + 112;
        return x >= (double) xCheck && y >= (double) yCheck && x < (double) xCheckMax && y < (double) yCheckMax;
    }

    protected void setScrollOffs(float val) {
        this.scrollOffs = Mth.clamp(val, 0.0F, 1.0F);
    }

    public void scrollTo(float pos) {
        int i = (enchantmentEntryList.size() + 1 - 1) - 7;
        int j = (int) ((double) (pos * (float) i) + 0.5D);

        if (j < 0) {
            j = 0;
        }

        for (int y = 0; y < enchantmentEntryList.size(); ++y) {
            if (j == 0) {
                enchantmentEntryList.get(y).setY((topPos + 25) + (y * 19));
            } else {
                enchantmentEntryList.get(y).setY((topPos + 25) + ((y - j) * 19));
            }
        }
    }

    @Override
    protected void containerTick() {
        searchBox.tick();
    }

    public int getTopPos() {
        return topPos;
    }
    public int getImageWidth() {
        return imageWidth;
    }

    public class EnchantmentButton extends Button {
        Enchantment enchant;
        final Button inc;
        final Button dec;
        public int level = 1;

        protected EnchantmentButton(int x, int y, int wid, int len, Component component, OnPress onPress, CreateNarration createNarration) {
            super(x, y, wid, len, component, onPress, createNarration);
            inc = Button.builder(Component.literal("+"), (tst) -> {
                level = Math.min(enchant.getMaxLevel(), level + 1);
                super.setMessage(enchant.getFullname(level));
            }).pos(x - 10, y).size(10, 19).build();

            dec = Button.builder(Component.literal("-"), (tst) -> {
                level = Math.max(enchant.getMinLevel(), level - 1);
                super.setMessage(enchant.getFullname(level));
            }).pos(x - 20, y).size(10, 19).build();
            addRenderableWidget(inc);
            addRenderableWidget(dec);
            inc.visible = false;
            dec.visible = false;
        }

        protected EnchantmentButton(int x, int y, Component component, OnPress onPress, CreateNarration createNarration, Enchantment ench) {
            this(x, y, 90, 19, component, onPress, createNarration);
            enchant = ench;
            if (enchant.getMaxLevel() == 1) {
                inc.active = false;
                dec.active = false;
            }
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            if ((this.getY() < (topPos + 24) + (7 * 19)) && getY() > (topPos + 24)) {
                super.renderWidget(poseStack, mouseX, mouseY, partialTick);
            }
        }

        @Override
        public void setPosition(int x, int y) {
            super.setPosition(x, y);
            dec.setPosition(x, y);
            inc.setPosition(x, y);
        }

        @Override
        public void setX(int x) {
            super.setX(x);
            dec.setX(x-10);
            inc.setX(x-20);
        }

        @Override
        public void setY(int y) {
            super.setY(y);
            inc.setY(y);
            dec.setY(y);
            if ((this.getY() < (topPos + 24) + (7 * 19)) && getY() > (topPos + 24)) {
                inc.visible = true;
                dec.visible = true;
            } else {
                inc.visible = false;
                dec.visible = false;
            }
        }



        public void removeButton() {
            removeWidget(inc);
            removeWidget(dec);
            removeWidget(this);
        }

    }

}
