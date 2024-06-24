package net.ramgames.tomereader.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.ramgames.tomereader.TomeReader;
import net.ramgames.tomereader.payloads.ItemStackSyncS2CLoad;
import net.ramgames.tomereader.client.screens.LecternEnchantedBookScreen;
import net.ramgames.tomereader.screenhandlers.LecternEnchantedBookScreenHandler;

public class TomeReaderClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(LecternEnchantedBookScreenHandler.LECTERN_ENCHANTED_BOOK_SCREEN_HANDLER, LecternEnchantedBookScreen::new);
        PayloadTypeRegistry.playS2C().register(ItemStackSyncS2CLoad.PACKET_ID, ItemStackSyncS2CLoad.PACKET_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ItemStackSyncS2CLoad.PACKET_ID, ((payload, context) -> {
            if(context.player().getWorld() == null) return;
            if(context.player().getWorld().getBlockEntity(payload.pos()) instanceof LecternBlockEntity lectern) {
                lectern.setBook(payload.stack());
                lectern.markDirty();
            }}));
    }
}
