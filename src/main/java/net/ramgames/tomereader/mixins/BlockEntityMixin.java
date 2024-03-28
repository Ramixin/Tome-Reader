package net.ramgames.tomereader.mixins;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ramgames.tomereader.TomeReader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.ramgames.tomereader.TomeReader.ITEM_SYNC;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {


    @Shadow @Nullable public abstract World getWorld();

    @Shadow public abstract BlockPos getPos();

    @Shadow public abstract NbtCompound createNbt();

    @Inject(method = "markDirty()V", at = @At("TAIL"))
    private void addPacketToMarkDirty(CallbackInfo ci) {
        if(!(((BlockEntity)(Object)this) instanceof LecternBlockEntity lectern)) return;
        if(!this.getWorld().isClient()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeBlockPos(getPos());
            data.writeItemStack(lectern.getBook());
            for(ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) getWorld(), getPos())) {
                ServerPlayNetworking.send(player, ITEM_SYNC, data);
            }
        }
    }

    @Inject(method = "toInitialChunkDataNbt", at = @At("HEAD"), cancellable = true)
    private void addLecternInitialChunkDataNbt(CallbackInfoReturnable<NbtCompound> cir) {
        if(!(((BlockEntity)(Object)this) instanceof LecternBlockEntity lectern)) return;
        cir.setReturnValue(this.createNbt());
    }
    @Inject(method = "toUpdatePacket", at = @At("HEAD"), cancellable = true)
    private void addLecternUpdatePacket(CallbackInfoReturnable<Packet<ClientPlayPacketListener>> cir) {
        if(!(((BlockEntity)(Object)this) instanceof LecternBlockEntity lectern)) return;
        cir.setReturnValue(BlockEntityUpdateS2CPacket.create((BlockEntity) (Object)this));
    }
}
