package net.ashindigo.prismatic.networking;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.entity.EnchanterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.List;

public class EnchantPacket extends BaseC2SMessage {

    private final BlockPos blockPos;
    private final List<EnchantmentInstance> enchantments;

    public EnchantPacket(BlockPos blockPos, List<EnchantmentInstance> enchantments) {
        this.blockPos = blockPos;
        this.enchantments = enchantments;
    }

    public EnchantPacket(FriendlyByteBuf buf) {
        blockPos = buf.readBlockPos();
        enchantments = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            enchantments.add(new EnchantmentInstance(Enchantment.byId(buf.readInt()), buf.readInt()));
        }
    }

    @Override
    public MessageType getType() {
        return PrismaticEnchanterMod.ENCHANT_PACKET;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(enchantments.size());
        enchantments.forEach(ench -> {
            buf.writeInt(BuiltInRegistries.ENCHANTMENT.getId(ench.enchantment));
            buf.writeInt(ench.level);
        });
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        EnchanterEntity blockEntity = (EnchanterEntity) context.getPlayer().level.getBlockEntity(blockPos);
        if (blockEntity.getItem(0).is(Items.BOOK)) {
            ItemStack book = Items.ENCHANTED_BOOK.getDefaultInstance();
            enchantments.forEach(ench -> EnchantedBookItem.addEnchantment(book, ench));
            blockEntity.setItem(0, book);
        } else {
            enchantments.forEach(ench -> blockEntity.getItem(0).enchant(ench.enchantment, ench.level));
        }
    }
}
