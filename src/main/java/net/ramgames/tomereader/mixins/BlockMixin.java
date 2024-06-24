package net.ramgames.tomereader.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.ramgames.tomereader.TomeReader;
import net.ramgames.tomereader.mixinhotswap.BlockMixinHotSwapper;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Debug(export = true)
@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "randomDisplayTick", at = @At("TAIL"))
    private void addEnchantParticlesToLectern(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockMixinHotSwapper.addEnchantParticlesToLectern(state,world,pos,random,ci);
    }

}
