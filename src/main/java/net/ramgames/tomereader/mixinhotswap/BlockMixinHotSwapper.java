package net.ramgames.tomereader.mixinhotswap;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface BlockMixinHotSwapper {

    static void addEnchantParticlesToLectern(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if(state.getBlock() != Blocks.LECTERN) return;
        if(!(world.getBlockEntity(pos) instanceof LecternBlockEntity lectern)) return;
        if(lectern.getBook().getItem() != Items.ENCHANTED_BOOK) return;
        if(random.nextBoolean()) return;
        for(int i = 0; i < 3; i++) {
            double xChange = (random.nextBoolean() ? -0.5 : 0.5) * random.nextDouble();
            double yChange = (random.nextBoolean() ? -0.5 : 0.5) * random.nextDouble();
            double zChange = (random.nextBoolean() ? -0.5 : 0.5) * random.nextDouble();
            double d = (double)pos.getX() + xChange+0.5;
            double e = (double)pos.getY() + yChange+2.5;
            double f = (double)pos.getZ() + zChange+0.5;
            double g = (xChange-0.5);
            double h = (yChange-0.6);
            double j = (zChange-0.5);
            world.addParticle(ParticleTypes.ENCHANT, d, e, f, g, h, j);
        }
    }
}
