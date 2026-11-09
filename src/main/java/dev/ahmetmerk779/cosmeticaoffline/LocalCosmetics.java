package dev.ahmetmerk779.cosmeticaoffline;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Owns the local-only cosmetic profile. It deliberately does not contact
 * Mojang, Cosmetica, or a third-party authentication service.
 */
public final class LocalCosmetics {
    private static final Path ROOT = FabricLoader.getInstance().getConfigDir().resolve("cosmetica-offline");
    private static final Path PROFILE_FILE = ROOT.resolve("profile-id.txt");
    private static final Path CAPES_DIR = ROOT.resolve("capes");
    private static UUID profileId;

    private LocalCosmetics() {}

    public static void initialize() {
        try {
            Files.createDirectories(CAPES_DIR);
            profileId = loadOrCreateProfileId();
            createGuideIfMissing();
        } catch (IOException exception) {
            CosmeticaOffline.LOGGER.error("Could not initialize local cosmetics storage", exception);
        }
    }

    private static UUID loadOrCreateProfileId() throws IOException {
        if (Files.isRegularFile(PROFILE_FILE)) {
            try {
                return UUID.fromString(Files.readString(PROFILE_FILE).trim());
            } catch (IllegalArgumentException ignored) {
                CosmeticaOffline.LOGGER.warn("Invalid local profile ID; creating a new one.");
            }
        }

        UUID generated = UUID.randomUUID();
        Files.writeString(PROFILE_FILE, generated.toString());
        return generated;
    }

    private static void createGuideIfMissing() throws IOException {
        Path guide = ROOT.resolve("HOW_TO_ADD_CAPE.txt");
        if (!Files.exists(guide)) {
            Files.writeString(guide, """
                COSMETICA OFFLINE - LOCAL COSMETICS

                This folder belongs to your local client only.
                Put cape PNG files in the 'capes' folder.

                Recommended cape image: 64x32 PNG (classic Minecraft cape layout).
                The cape renderer and in-game wardrobe are being implemented.
                No file in this folder is uploaded or shared with a server.
                """);
        }
    }

    public static UUID getProfileId() {
        return profileId;
    }

    public static Path getRootDirectory() {
        return ROOT;
    }

    public static Path getCapesDirectory() {
        return CAPES_DIR;
    }
}
