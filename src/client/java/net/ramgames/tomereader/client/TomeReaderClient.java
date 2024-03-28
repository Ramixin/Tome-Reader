package net.ramgames.tomereader.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registry;
import net.ramgames.tomereader.TomeReader;
import net.ramgames.tomereader.client.packets.ItemStackSyncS2CPacket;
import net.ramgames.tomereader.client.screens.LecternEnchantedBookScreen;
import net.ramgames.tomereader.screenhandlers.LecternEnchantedBookScreenHandler;

public class TomeReaderClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(LecternEnchantedBookScreenHandler.LECTERN_ENCHANTED_BOOK_SCREEN_HANDLER, LecternEnchantedBookScreen::new);
        ClientPlayNetworking.registerGlobalReceiver(TomeReader.ITEM_SYNC, ItemStackSyncS2CPacket::receive);
    }
}
