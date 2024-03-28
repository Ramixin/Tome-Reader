package net.ramgames.tomereader.client.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.ramgames.tomereader.TomeReader;

public class ItemStackSyncS2CPacket {

    public static void receive(net.minecraft.client.MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender) {
        BlockPos position = buf.readBlockPos();
        if(minecraftClient.world == null) return;
        if(minecraftClient.world.getBlockEntity(position) instanceof LecternBlockEntity lectern) {
            ItemStack stack = buf.readItemStack();
            lectern.setBook(stack);
            lectern.markDirty();
        }
    }
}

