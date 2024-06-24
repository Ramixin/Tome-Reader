package net.ramgames.tomereader;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ramgames.tomereader.screenhandlers.LecternEnchantedBookScreenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomeReader implements ModInitializer {

    public static final String MOD_ID = "tome_reader";
    public static final Logger LOGGER = LoggerFactory.getLogger("Tome Reader");

    public static final Identifier ITEM_SYNC = Identifier.of(MOD_ID, "item_sync");
    @Override
    public void onInitialize() {
        LOGGER.info("Tome Reader is preparing to share the ancient knowledge");
        Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "lectern_enchanted_book"), LecternEnchantedBookScreenHandler.LECTERN_ENCHANTED_BOOK_SCREEN_HANDLER);
    }

    public static LecternAccess getAccess(LecternBlockEntity blockEntity) {
        return (LecternAccess) blockEntity;
    }
}
