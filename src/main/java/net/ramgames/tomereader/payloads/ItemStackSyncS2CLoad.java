package net.ramgames.tomereader.payloads;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.ramgames.tomereader.TomeReader;

public record ItemStackSyncS2CLoad(BlockPos pos, ItemStack stack) implements CustomPayload {

    public static final CustomPayload.Id<ItemStackSyncS2CLoad> PACKET_ID = new CustomPayload.Id<>(TomeReader.ITEM_SYNC);
    public static final PacketCodec<RegistryByteBuf, ItemStackSyncS2CLoad> PACKET_CODEC = PacketCodec.of(ItemStackSyncS2CLoad::write, ItemStackSyncS2CLoad::new);



    public ItemStackSyncS2CLoad(RegistryByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean() ? ItemStack.EMPTY : ItemStack.PACKET_CODEC.decode(buf));
    }

    public static void write(ItemStackSyncS2CLoad load,RegistryByteBuf buf) {
        buf.writeBlockPos(load.pos);
        buf.writeBoolean(load.stack.isEmpty());
        if(!load.stack.isEmpty()) ItemStack.PACKET_CODEC.encode(buf, load.stack);
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}

