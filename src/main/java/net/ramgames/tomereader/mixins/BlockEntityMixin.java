package net.ramgames.tomereader.mixins;

import com.llamalad7.mixinextras.sugar.Local;
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
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ramgames.tomereader.payloads.ItemStackSyncS2CLoad;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.ramgames.tomereader.TomeReader.ITEM_SYNC;

@SuppressWarnings("UnreachableCode")
@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {


    @Shadow @Nullable public abstract World getWorld();

    @Shadow public abstract BlockPos getPos();

    @Shadow public abstract NbtCompound createNbt(RegistryWrapper.WrapperLookup registryLookup);

    @Inject(method = "markDirty()V", at = @At("TAIL"))
    private void addPacketToMarkDirty(CallbackInfo ci) {
        if(!(((BlockEntity)(Object)this) instanceof LecternBlockEntity lectern)) return;
        if(this.getWorld() == null) return;
        if(!this.getWorld().isClient()) {
            ItemStackSyncS2CLoad payload = new ItemStackSyncS2CLoad(getPos(), lectern.getBook());
            for(ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) getWorld(), getPos())) {
                ServerPlayNetworking.send(player, payload);
            }
        }
    }

    @Inject(method = "toInitialChunkDataNbt", at = @At("HEAD"), cancellable = true)
    private void addLecternInitialChunkDataNbt(CallbackInfoReturnable<NbtCompound> cir, @Local(argsOnly = true) RegistryWrapper.WrapperLookup registryLookup) {
        if(!(((BlockEntity)(Object)this) instanceof LecternBlockEntity ignored)) return;
        cir.setReturnValue(this.createNbt(registryLookup));
    }
    @Inject(method = "toUpdatePacket", at = @At("HEAD"), cancellable = true)
    private void addLecternUpdatePacket(CallbackInfoReturnable<Packet<ClientPlayPacketListener>> cir) {
        if(!(((BlockEntity)(Object)this) instanceof LecternBlockEntity ignored)) return;
        cir.setReturnValue(BlockEntityUpdateS2CPacket.create((BlockEntity) (Object)this));
    }
}
