package net.ramgames.tomereader.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.ramgames.tomereader.mixinhotswap.BlockMixinHotSwapper;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "randomDisplayTick", at = @At("TAIL"))
    private void addEnchantParticlesToLectern(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockMixinHotSwapper.addEnchantParticlesToLectern(state,world,pos,random,ci);
    }

}
