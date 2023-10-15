package net.ashindigo.prismatic.networking;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.ashindigo.prismatic.PrismaticEnchanterMod;
import net.ashindigo.prismatic.client.screen.EnchanterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class DoneEnchantPacket extends BaseS2CMessage {

    final BlockPos pos;

    public DoneEnchantPacket(BlockPos blockPos) {
        pos = blockPos;
    }

    public DoneEnchantPacket(FriendlyByteBuf friendlyByteBuf) {
        pos = friendlyByteBuf.readBlockPos();
    }

    @Override
    public MessageType getType() {
        return PrismaticEnchanterMod.DONE_ENCHANT_PACKET;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
//        EnchanterMenu containerMenu = (EnchanterMenu) context.getPlayer().containerMenu;
//        containerMenu.refreshSearchResults.call();
        EnchanterScreen screen = (EnchanterScreen) Minecraft.getInstance().screen;
        screen.refreshSearchResults();
    }
}
