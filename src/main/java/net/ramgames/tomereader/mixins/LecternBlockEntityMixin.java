package net.ramgames.tomereader.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.ramgames.tomereader.LecternAccess;
import net.ramgames.tomereader.screenhandlers.LecternEnchantedBookScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin extends BlockEntity implements LecternAccess {
    @Unique
    public int ticks;
    @Unique
    public float nextPageAngle;
    @Unique
    public float pageAngle;
    @Unique
    public float flipRandom;
    @Unique
    public float flipTurn;
    @Unique
    public float nextPageTurningSpeed;
    @Unique
    public float pageTurningSpeed;
    @Unique
    public float bookRotation;
    @Unique
    public float lastBookRotation;
    @Unique
    public float targetBookRotation;

    @Shadow @Final private Inventory inventory;

    public LecternBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "createMenu", at = @At("HEAD"), cancellable = true)
    private void openEnchantedBookScreen(int i, PlayerInventory playerInventory, PlayerEntity playerEntity, CallbackInfoReturnable<ScreenHandler> cir) {
        if(this.inventory.getStack(0).getItem() instanceof EnchantedBookItem) cir.setReturnValue(new LecternEnchantedBookScreenHandler(i, playerInventory, this.inventory));
    }

    @Override
    public int tomeReader$getTicks() {
        return ticks;
    }

    @Override
    public float tomeReader$getNextPageAngle() {
        return nextPageAngle;
    }

    @Override
    public float tomeReader$getPageAngle() {
        return pageAngle;
    }

    @Override
    public float tomeReader$getFlipRandom() {
        return flipRandom;
    }

    @Override
    public float tomeReader$getFlipTurn() {
        return flipTurn;
    }

    @Override
    public float tomeReader$getNextPageTurningSpeed() {
        return nextPageTurningSpeed;
    }

    @Override
    public float tomeReader$getPageTurningSpeed() {
        return pageTurningSpeed;
    }

    @Override
    public float tomeReader$getBookRotation() {
        return bookRotation;
    }

    @Override
    public float tomeReader$getLastBookRotation() {
        return lastBookRotation;
    }

    @Override
    public float tomeReader$getTargetBookRotation() {
        return targetBookRotation;
    }

    @Override
    public void tomeReader$setTicks(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public void tomeReader$setNextPageAngle(float nextPageAngle) {
        this.nextPageAngle = nextPageAngle;
    }

    @Override
    public void tomeReader$setPageAngle(float pageAngle) {
        this.pageAngle = pageAngle;
    }

    @Override
    public void tomeReader$setFlipRandom(float flipRandom) {
        this.flipRandom = flipRandom;
    }

    @Override
    public void tomeReader$setFlipTurn(float flipTurn) {
        this.flipTurn = flipTurn;
    }

    @Override
    public void tomeReader$setNextPageTurningSpeed(float nextPageTurningSpeed) {
        this.nextPageTurningSpeed = nextPageTurningSpeed;
    }

    @Override
    public void tomeReader$setPageTurningSpeed(float pageTurningSpeed) {
        this.pageTurningSpeed = pageTurningSpeed;
    }

    @Override
    public void tomeReader$setBookRotation(float bookRotation) {
        this.bookRotation = bookRotation;
    }

    @Override
    public void tomeReader$setLastBookRotation(float lastBookRotation) {
        this.lastBookRotation = lastBookRotation;
    }

    @Override
    public void tomeReader$setTargetBookRotation(float targetBookRotation) {
        this.targetBookRotation = targetBookRotation;
    }


}
