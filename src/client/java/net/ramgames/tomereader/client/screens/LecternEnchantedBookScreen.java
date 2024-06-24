package net.ramgames.tomereader.client.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.ramgames.tomereader.BookModelHelper;
import net.ramgames.tomereader.screenhandlers.LecternEnchantedBookScreenHandler;

import java.util.ArrayList;
import java.util.List;

public class LecternEnchantedBookScreen extends HandledScreen<LecternEnchantedBookScreenHandler> {

    private static final Identifier BOOK_TEXTURE = Identifier.of("textures/entity/enchanting_table_book.png");
    private BookModel BOOK_MODEL;
    public float nextPageAngle;
    public float pageAngle;
    public float nextPageTurningSpeed;
    public float pageTurningSpeed;
    public int ticks;
    private float pageRotationSpeed;
    private float approximatePageAngle;
    private final Random random = Random.create();
    private final ItemStack mainHandStack;
    private ButtonWidget transferButton;
    private int disabledIndex = 0;
    private final int playerXPLevel;
    private final boolean isCreative;

    private static final Identifier ENCHANTMENT_TABLE_TEXTURE = Identifier.of("minecraft:textures/gui/container/enchanting_table.png");

    public LecternEnchantedBookScreen(LecternEnchantedBookScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        if(this.client != null) this.BOOK_MODEL = new BookModel(this.client.getEntityModelLoader().getModelPart(EntityModelLayers.BOOK));
        this.backgroundWidth = 256;
        this.backgroundHeight = 256;
        this.mainHandStack = inventory.getMainHandStack();
        this.playerXPLevel = inventory.player.experienceLevel;
        this.isCreative = inventory.player.getAbilities().creativeMode;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        Pair<List<Text>, Integer> tooltip = generateToolTip();
        int y = this.y+(backgroundHeight)/2+5;
        y -= 10 * (tooltip.getLeft().size()-3);
        drawBook(context, this.width/2, y, delta);
        context.drawTooltip(this.textRenderer, tooltip.getLeft(), (context.getScaledWindowWidth()-tooltip.getRight()-22)/2, this.height/2+30-(tooltip.getLeft().size()-3)*10);
        if(transferButton != null) {
            context.drawTexture(ENCHANTMENT_TABLE_TEXTURE,this.width / 2 - 98, 222, 32, transferButton.active ? 223 : 239, 16, 16);
        }


        // 34 x, 224 y
    }

    public void addCloseButton() {
        if(this.client == null) return;
        if(this.client.player == null) return;
        if (this.client.player.canModifyBlocks()) {
            this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.close()).dimensions(this.width / 2 - 100, 196, 98, 20).build());
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("lectern.take_book"), (button) -> {
                this.sendButtonPressPacket(3);
                this.close();
            }).dimensions(this.width / 2 + 2, 196, 98, 20).build());
            transferButton = ButtonWidget.builder(Text.literal("Transfer Enchantment"), (button) -> {
                this.sendButtonPressPacket(4);
                this.close();
            }).dimensions(this.width / 2 - 100, 220, 200, 20).build();
            this.addDrawableChild(transferButton);
        } else {
            this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.close()).dimensions(this.width / 2 - 100, 196, 200, 20).build());
        }
    }

    private void sendButtonPressPacket(int id) {
        if(this.client != null && this.client.interactionManager != null) this.client.interactionManager.clickButton(this.handler.syncId, id);
    }

    private Pair<List<Text>, Integer> generateToolTip() {
        List<Text> list = new ArrayList<>();
        ItemStack stack = this.handler.getInventory().getStack(0);
        MutableText name = Text.empty().append(stack.getName()).formatted(stack.getRarity().getFormatting());
        if(stack.contains(DataComponentTypes.CUSTOM_NAME)) name.formatted(Formatting.ITALIC);
        list.add(name);
        if(stack.contains(DataComponentTypes.STORED_ENCHANTMENTS))
            stack.get(DataComponentTypes.STORED_ENCHANTMENTS).appendTooltip(Item.TooltipContext.DEFAULT, list::add, TooltipType.BASIC);
        int max = 0;
        for(Text text : list) max = Math.max(max, textRenderer.getWidth(text));
        return new Pair<>(list, max);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }

    private void drawBook(DrawContext context, int x, int y, float delta) {
        if(this.BOOK_MODEL == null) return;
        float f = MathHelper.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
        DiffuseLighting.method_34742();
        context.getMatrices().push();
        float oscillate = MathHelper.sin(ticks/8f);
        context.getMatrices().translate((float)x, (float)y - 31.0F+oscillate, 100.0F);
        float h = 120.0F;
        context.getMatrices().scale(-h, h, h);
        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(25.0F));
        context.getMatrices().translate((1.0F - f) * 0.2F, (1.0F - f) * 0.1F, (1.0F - f) * 0.25F);
        float i = -(1.0F - f) * 90.0F - 90.0F;
        context.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i));
        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        BookModelHelper.setBookAngles(this.BOOK_MODEL, delta, pageTurningSpeed, nextPageTurningSpeed, pageAngle, nextPageAngle);
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(this.BOOK_MODEL.getLayer(BOOK_TEXTURE));
        this.BOOK_MODEL.render(context.getMatrices(), vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV);
        context.draw();
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

    @Override
    protected void handledScreenTick() {
        float[] update = BookModelHelper.updateBookParameters(random, approximatePageAngle, nextPageAngle, nextPageTurningSpeed, pageRotationSpeed, ticks == 0);
        approximatePageAngle = update[0];
        nextPageAngle = update[1];
        pageAngle = update[2];
        pageTurningSpeed = update[3];
        nextPageTurningSpeed = update[4];
        pageRotationSpeed = update[5];
        ++this.ticks;
        updateTransferButton();
        super.handledScreenTick();
    }

    @Override
    protected void init() {
        if(this.client != null) this.BOOK_MODEL = new BookModel(this.client.getEntityModelLoader().getModelPart(EntityModelLayers.BOOK));
        addCloseButton();
        updateTransferButton();
        super.init();
    }

    private void updateTransferButton() {
        if(this.transferButton == null) return;
        if(handler.getInventory().getStack(0).contains(DataComponentTypes.STORED_ENCHANTMENTS) && handler.getInventory().getStack(0).get(DataComponentTypes.STORED_ENCHANTMENTS).getSize() <= 1) {
            if(transferButton.active || disabledIndex != 1) {
                transferButton.active = false;
                transferButton.setTooltip(Tooltip.of(Text.literal("Cannot transfer single enchantment").formatted(Formatting.WHITE)));
                disabledIndex = 1;
            }
        } else if(this.mainHandStack.getItem() != Items.BOOK || this.mainHandStack.getCount() != 1) {
            if(transferButton.active || disabledIndex != 2) {
                transferButton.active = false;
                transferButton.setTooltip(Tooltip.of(Text.literal("Hold one book in your main hand").formatted(Formatting.WHITE)));
                disabledIndex = 2;
            }
        } else if(this.playerXPLevel < 3 && !this.isCreative) {
            if(transferButton.active || disabledIndex != 3) {
                transferButton.active = false;
                transferButton.setTooltip(Tooltip.of(Text.literal("not enough XP levels").formatted(Formatting.WHITE)));
                disabledIndex = 3;
            }
        } else {
            transferButton.active = true;
            transferButton.setTooltip(Tooltip.of(Text.empty()));
            disabledIndex = 0;
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
