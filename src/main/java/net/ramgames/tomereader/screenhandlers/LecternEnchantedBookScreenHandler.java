package net.ramgames.tomereader.screenhandlers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.ramgames.tomereader.TomeReader;

import java.util.Map;

public class LecternEnchantedBookScreenHandler extends ScreenHandler {

    public static final ScreenHandlerType<LecternEnchantedBookScreenHandler> LECTERN_ENCHANTED_BOOK_SCREEN_HANDLER = new ScreenHandlerType<>(LecternEnchantedBookScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
    private final Inventory inventory;
    private final Slot slot;
    protected LecternEnchantedBookScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    public LecternEnchantedBookScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(LECTERN_ENCHANTED_BOOK_SCREEN_HANDLER, syncId);
        checkSize(inventory, 1);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        slot = new Slot(this.inventory, 0, Integer.MAX_VALUE, Integer.MAX_VALUE) {
            public void markDirty() {
                super.markDirty();
                LecternEnchantedBookScreenHandler.this.onContentChanged(this.inventory);
            }
        };
        this.addSlot(slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return LECTERN_ENCHANTED_BOOK_SCREEN_HANDLER;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
         if(id == 3) {
            if (!player.canModifyBlocks()) return false;
            ItemStack itemStack = this.inventory.removeStack(0);
            this.inventory.markDirty();
            if (!player.getInventory().insertStack(itemStack)) player.dropItem(itemStack, false);
            return true;
        }
        else if(id == 4) {
            ItemStack mainStack = player.getMainHandStack();
            ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
            if(mainStack.getItem() != Items.BOOK || mainStack.getCount() != 1 || (player.experienceLevel < 3 && !player.getAbilities().creativeMode)) return false;
            player.addExperienceLevels(-3);
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1, Math.max(1F, player.getRandom().nextFloat()+0.3F));
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(this.inventory.getStack(0));
            Enchantment transferEnchant = enchantments.keySet().stream().toList().get(player.getRandom().nextBetween(0, enchantments.size()-1));
            EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentLevelEntry(transferEnchant, enchantments.get(transferEnchant)));
            player.setStackInHand(Hand.MAIN_HAND, enchantedBook);
            enchantments.remove(transferEnchant);
            ItemStack referenceStack = this.inventory.getStack(0);
            referenceStack.removeSubNbt(EnchantedBookItem.STORED_ENCHANTMENTS_KEY);
            EnchantmentHelper.set(enchantments, referenceStack);
            inventory.markDirty();
            slot.markDirty();
            return true;
        }
        return super.onButtonClick(player, id);
    }
}
