package net.ramgames.tomereader.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ramgames.tomereader.LecternAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlock.class)
public class LecternBlockMixin {

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/LecternBlock;putBookIfAbsent(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/item/ItemStack;)Z", shift = At.Shift.BEFORE))
    private void applyTagChange(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
        if(stack.isOf(Items.ENCHANTED_BOOK) && world.getBlockEntity(pos) != null) ((LecternAccess)world.getBlockEntity(pos)).tomeReader$setIsTomeReaderLectern(true);
    }
}
