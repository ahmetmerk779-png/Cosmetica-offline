package dev.ahmetmerk779.cosmeticaoffline;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CosmeticaOffline implements ClientModInitializer {
    public static final String MOD_ID = "cosmetica_offline";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LocalCosmetics.initialize();
        LocalCapeTextures.reload();
        ScreenIntegration.register();
        LOGGER.info("Cosmetica Offline initialized. Local profile: {}", LocalCosmetics.getProfileId());
    }
}
