package net.ramgames.tomereader.client.mixins.client;

import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.LecternBlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.ramgames.tomereader.LecternAccess;
import net.ramgames.tomereader.TomeReader;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

import static net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer.BOOK_TEXTURE;

@Debug(export = true)
@Mixin(LecternBlockEntityRenderer.class)
public abstract class LecternBlockEntityRendererMixin implements BlockEntityRenderer<LecternBlockEntity> {

    @Shadow @Final private BookModel book;
    @Unique
    public HashMap<BlockPos, Random> randoms = new HashMap<>();

    @Inject(method = "render(Lnet/minecraft/block/entity/LecternBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At("HEAD"), cancellable = true)
    private void flipBookPages(LecternBlockEntity lecternBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        ItemStack stack = lecternBlockEntity.getBook();
        if(stack.getItem() != Items.ENCHANTED_BOOK) return;
        LecternAccess access = TomeReader.getAccess(lecternBlockEntity);
        if(stack.get(DataComponentTypes.CUSTOM_DATA) != null && stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt().getBoolean("sealed")) renderClosedBook(matrixStack, vertexConsumerProvider, i ,j, ci);
        else renderNonClosedBook(access, lecternBlockEntity, f, matrixStack, vertexConsumerProvider, i, j, ci);
    }

    @Unique
    private void renderClosedBook(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        matrixStack.push();
        matrixStack.translate(0.5F, 1.0625F, 0.5F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(115));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0));
        matrixStack.translate(-0.19F, 0.1, -0.06F);
        book.setPageAngles(0,0,0,0);
        VertexConsumer vertexConsumer = BOOK_TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
        book.renderBook(matrixStack, vertexConsumer, i, j, -1);
        matrixStack.pop();
        ci.cancel();
    }

    @Unique
    private void renderNonClosedBook(LecternAccess access, LecternBlockEntity lecternBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        access.tomeReader$setLastBookRotation(access.tomeReader$getBookRotation());
        access.tomeReader$setTargetBookRotation(access.tomeReader$getTargetBookRotation()+0.02F);

        while(access.tomeReader$getBookRotation() >= 3.1415927F) access.tomeReader$setBookRotation(access.tomeReader$getBookRotation() - 6.2831855F);

        while(access.tomeReader$getBookRotation() < -3.1415927F) access.tomeReader$setBookRotation(access.tomeReader$getBookRotation() + 6.2831855F);

        while(access.tomeReader$getTargetBookRotation() >= 3.1415927F) access.tomeReader$setTargetBookRotation(access.tomeReader$getTargetBookRotation() - 6.2831855F);

        while(access.tomeReader$getTargetBookRotation() < -3.1415927F) access.tomeReader$setTargetBookRotation(access.tomeReader$getTargetBookRotation() + 6.2831855F);

        float g;
        for(g = access.tomeReader$getTargetBookRotation() - access.tomeReader$getBookRotation(); g >= 3.1415927F; g -= 6.2831855F);

        while(g < -3.1415927F) g += 6.2831855F;

        access.tomeReader$setBookRotation(access.tomeReader$getBookRotation()+(g * 0.4F));

        access.tomeReader$setTicks(access.tomeReader$getTicks()+1);
        access.tomeReader$setPageAngle(access.tomeReader$getNextPageAngle());
        float h = (access.tomeReader$getFlipRandom() - access.tomeReader$getNextPageAngle()) * 0.4F;
        h = MathHelper.clamp(h, -0.2F, 0.2F);
        access.tomeReader$setFlipTurn(access.tomeReader$getFlipTurn()+(h - access.tomeReader$getFlipTurn()) * 0.5F);
        access.tomeReader$setNextPageAngle(access.tomeReader$getNextPageAngle()+access.tomeReader$getFlipTurn());
        Random random = randoms.get(lecternBlockEntity.getPos());
        if(random == null) {
            random = Random.create();
            randoms.put(lecternBlockEntity.getPos(), random);
        }

        if (random.nextInt(75) == 0) {
            float ff = access.tomeReader$getFlipRandom();
            do {
                access.tomeReader$setFlipRandom(access.tomeReader$getFlipRandom()+(float)(random.nextInt(4) - random.nextInt(4)));
            } while(ff == access.tomeReader$getFlipRandom());
        }
        matrixStack.push();
        matrixStack.translate(0.5F, 1.0625F, 0.5F);
        float gg = lecternBlockEntity.getCachedState().get(LecternBlock.FACING).rotateYClockwise().asRotation();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-gg));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(67.5F));
        matrixStack.translate(0.0F, -0.125F, 0.0F);
        float l = MathHelper.lerp(f, access.tomeReader$getPageAngle(), access.tomeReader$getNextPageAngle());
        float m = MathHelper.fractionalPart(l + 0.25F) * 1.6F - 0.3F;
        float n = MathHelper.fractionalPart(l + 0.75F) * 1.6F - 0.3F;
        float o = MathHelper.lerp(f, 1.2F, 1.2F);
        this.book.setPageAngles(gg, MathHelper.clamp(m, 0.0F, 1.0F), MathHelper.clamp(n, 0.0F, 1.0F), o);
        VertexConsumer vertexConsumer = BOOK_TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
        this.book.renderBook(matrixStack, vertexConsumer, i, j, -1);
        matrixStack.pop();
        ci.cancel();
    }
}
