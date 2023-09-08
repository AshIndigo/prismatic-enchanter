package net.ashindigo.prismatic.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.menu.EnchanterMenu;
import net.ashindigo.prismatic.networking.EnchantPacket;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
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

    private static final ResourceLocation ENCHANTING_TABLE_LOCATION = PrismaticEnchanterMod.makeResourceLocation("textures/gui/enchanter.png");
    public static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    public EditBox searchBox;
    Button enchantBtn;
    StringWidget xpCost;
    public float scrollOffs;
    public boolean scrolling;

    public List<Enchantment> enchantmentList = new ArrayList<>();
    public List<EnchantmentButton> enchantmentEntryList = new ArrayList<>();
    public List<EnchantmentInstance> selected = new ArrayList<>();

    public EnchanterScreen(EnchanterMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.imageHeight = 165;
        this.imageWidth = 302;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        searchBox = new EditBox(this.font, this.leftPos + 174, this.topPos + 14, 108, 10, Component.translatable("text.search")); // 82
        addRenderableWidget(searchBox);
        enchantBtn = Button.builder(Component.literal("Enchant"), btn -> new EnchantPacket(menu.entity.getBlockPos(), selected).sendToServer()).size(45, 18).pos(this.leftPos + 34, this.topPos + 46).build();
        addRenderableWidget(enchantBtn);
        Button clearBtn = Button.builder(Component.literal("Clear"), inst -> {
            selected.clear();
            updateCostText();
        }).size(30, 18).pos(this.leftPos + 132, this.topPos + 46).build();
        addRenderableWidget(clearBtn);

        xpCost = new StringWidget(Component.literal("XP Cost: ").append(Component.literal(Integer.toString(getTotalCost()))), this.font) {
            @Override
            public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                fillGradient(poseStack, getX() - 2, getY() - 2, getX() + font.width(getMessage()) + 2, getY() + height + 2, -1072689136, -804253680);
                super.renderWidget(poseStack, mouseX, mouseY, partialTick);
            }
        }.alignCenter();
        xpCost.setPosition(this.leftPos + 14, this.topPos + 22);
        addRenderableWidget(xpCost);

        StringWidget review = new StringWidget(Component.literal("Review"), this.font) {
            @Override
            public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                fillGradient(poseStack, getX() - 2, getY() - 1, getX() + font.width(getMessage()) + 2, getY() + height + 2, -1072689136, -804253680);
                super.renderWidget(poseStack, mouseX, mouseY, partialTick);
            }
        }.alignCenter();
        review.setPosition(this.leftPos + 14, this.topPos + 34);
        addRenderableWidget(review);
        refreshSearchResults();
    }

    private void updateCostText() {
        xpCost.setMessage(Component.literal("XP Cost: ").append(Component.literal(Integer.toString(getTotalCost()))));
    }

    private int getTotalCost() {
        return selected.stream().mapToInt(inst -> inst.enchantment.getMinCost(inst.level)).sum();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float rT, int x, int y) {
        //this.renderBackground(poseStack);
        Lighting.setupForFlatItems();
        RenderSystem.setShaderTexture(0, ENCHANTING_TABLE_LOCATION);
        blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
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
        enchantmentEntryList.forEach(EnchantmentButton::removeButton);
        enchantmentEntryList.clear();
        String s = this.searchBox.getValue();
        AtomicInteger i = new AtomicInteger();
        if (menu.getSlot(0).hasItem()) {
            getAvailableEnchantmentResults(BuiltInRegistries.ENCHANTMENT.entrySet().stream().filter(resourceKeyEnchantmentEntry -> resourceKeyEnchantmentEntry.getValue().getFullname(1).getString().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))), menu.getSlot(0).getItem(), true).forEach(inst -> {
                if (isCompatible(EnchantmentHelper.getEnchantments(menu.getSlot(0).getItem()).keySet(), inst.enchantment)) {
                    addEnchantToList(inst.enchantment, i.getAndIncrement());
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
            if (!e.isCompatibleWith(addition))
                return false;
        }
        return true;
    }

    private void addEnchantToList(Enchantment entry, int i) {
        enchantmentList.add(entry);
        enchantmentEntryList.add(new EnchantmentButton(leftPos + 173 + 20 - 1, topPos + 25 + (i * 19), 91, 19, entry.getFullname(1), button -> {
            if (selected.stream().noneMatch(inst -> inst.enchantment.equals(entry))) { // TODO maybe replace based on ench level if different one is selected? // inst.level == ((EnchantmentButton) button).level &&
                selected.add(new EnchantmentInstance(entry, ((EnchantmentButton) button).level));
                updateCostText();
            }
        }, (supplier) -> entry.getFullname(1).copy(), entry));
    }

    // Scrolling

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            if (this.insideScrollbar(pMouseX, pMouseY)) {
                this.scrolling = enchantmentList.size() > 7;//this.menu.slots.size() > 90;
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
        //if (3 - 4 == 0) return true; // todo
        //int i = 1; // TODO
        if (enchantmentList.size() - 7 == 0) return true;
        int i = (enchantmentList.size() - 7) - 7;
        setScrollOffs((float) ((double) this.scrollOffs - pDelta / (double) i));
        scrollTo(scrollOffs);
        return true;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling) {
            int i = this.topPos + 18 + 7;
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
        int xCheck = i + 175 + 110;
        int yCheck = j + 18 + 7;
        int xCheckMax = xCheck + 14;
        int yCheckMax = yCheck + 112;
        return x >= (double) xCheck && y >= (double) yCheck && x < (double) xCheckMax && y < (double) yCheckMax;
    }

    protected void setScrollOffs(float val) {
        this.scrollOffs = Mth.clamp(val, 0.0F, 1.0F);
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
        int i = (enchantmentList.size() + 1 - 1) - 7;
        int j = (int) ((double) (pos * (float) i) + 0.5D);

        if (j < 0) {
            j = 0;
        }

        for (int y = 0; y < enchantmentList.size(); ++y) {
            if (j == 0) {

                //StorageCabinetExpectPlatform.setSlotY(slots.get(y * 9 + x), 18 + y * 18);
            } else {
                //StorageCabinetExpectPlatform.setSlotY(slots.get(y * 9 + x), 18 + (y - j) * 18);
            }
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        searchBox.tick();
    }

    public class EnchantmentButton extends Button {
        Enchantment enchant;
        Button inc;
        Button dec;
        public int level = 1;

        protected EnchantmentButton(int x, int y, int wid, int len, Component component, OnPress onPress, CreateNarration createNarration) {
            super(x, y, wid, len, component, onPress, createNarration);
            inc = Button.builder(Component.literal("+"), (tst) -> {
                level = Math.min(enchant.getMaxLevel(), level + 1);
                super.setMessage(enchant.getFullname(level));
            }).pos(x - 10, y).size(10, 19).build();//new Button(x - 10, y, 10, 19, Component.literal("+"), (tst) -> level++, (nar) -> component.copy());
            dec = Button.builder(Component.literal("-"), (tst) -> {
                level = Math.max(enchant.getMinLevel(), level - 1);
                super.setMessage(enchant.getFullname(level));
            }).pos(x - 20, y).size(10, 19).build();
            addRenderableWidget(inc);
            addRenderableWidget(dec);
            inc.visible = false;
            dec.visible = false;
        }

        protected EnchantmentButton(int x, int y, int wid, int len, Component component, OnPress onPress, CreateNarration createNarration, Enchantment ench) {
            this(x, y, wid, len, component, onPress, createNarration);
            enchant = ench;
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            if (this.getY() < (topPos + 24) + (7 * 19)) {
                super.renderWidget(poseStack, mouseX, mouseY, partialTick);
                inc.visible = true;
                dec.visible = true;
            } else {
                inc.visible = false;
                dec.visible = false;
            }
        }

        public Enchantment getEnchant() {
            return enchant;
        }

        public void setEnchant(Enchantment enchant) {
            this.enchant = enchant;
        }

        public void removeButton() {
            removeWidget(inc);
            removeWidget(dec);
            removeWidget(this);
        }

    }
}
