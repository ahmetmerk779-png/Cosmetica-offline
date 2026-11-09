package dev.ahmetmerk779.cosmeticaoffline;

import net.fabricmc.loader.api.FabricLoader;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class LocalCosmetics {
    private static final Path ROOT = FabricLoader.getInstance().getConfigDir().resolve("cosmetica-offline");
    private static final Path PROFILE_FILE = ROOT.resolve("profile-id.txt");
    private static final Path CAPES_DIR = ROOT.resolve("capes");
    private static final Path ACTIVE_CAPE_FILE = ROOT.resolve("active-cape.txt");
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
                COSMETICA OFFLINE - LOCAL CAPES

                1. Copy cape PNG files into the 'capes' folder, or import one
                   from the in-game wardrobe using its full file path.
                2. Supported classic cape sizes: 64x32 and 64x64 PNG.
                3. Select a cape in the wardrobe to save it as active.

                This is local client storage. Nothing is uploaded or shared.
                """);
        }
    }

    public static List<String> getCapeFiles() {
        try (var files = Files.list(CAPES_DIR)) {
            return files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".png"))
                    .map(path -> path.getFileName().toString())
                    .sorted(Comparator.naturalOrder())
                    .toList();
        } catch (IOException exception) {
            CosmeticaOffline.LOGGER.error("Could not list local capes", exception);
            return List.of();
        }
    }

    public static String getActiveCape() {
        try {
            if (Files.isRegularFile(ACTIVE_CAPE_FILE)) {
                String value = Files.readString(ACTIVE_CAPE_FILE).trim();
                if (!value.isEmpty() && getCapeFiles().contains(value)) return value;
            }
        } catch (IOException exception) {
            CosmeticaOffline.LOGGER.warn("Could not read active cape", exception);
        }
        return "";
    }

    public static void setActiveCape(String filename) {
        try {
            if (filename == null || filename.isBlank()) {
                Files.deleteIfExists(ACTIVE_CAPE_FILE);
                return;
            }
            String safeName = Path.of(filename).getFileName().toString();
            if (!safeName.toLowerCase(Locale.ROOT).endsWith(".png") || !getCapeFiles().contains(safeName)) {
                throw new IOException("Cape file does not exist in the local capes folder");
            }
            Files.writeString(ACTIVE_CAPE_FILE, safeName);
        } catch (IOException exception) {
            CosmeticaOffline.LOGGER.error("Could not save active cape", exception);
        }
    }

    public static String importCape(Path source) throws IOException {
        if (source == null || !Files.isRegularFile(source)) throw new IOException("Selected file does not exist");
        String original = source.getFileName().toString();
        if (!original.toLowerCase(Locale.ROOT).endsWith(".png")) throw new IOException("Only PNG cape files are supported");
        int width;
        int height;
        try (InputStream input = Files.newInputStream(source);
             NativeImage image = NativeImage.read(input)) {
            width = image.getWidth();
            height = image.getHeight();
        }
        if (width != 64 || (height != 32 && height != 64)) {
            throw new IOException("Cape size must be 64x32 or 64x64 pixels (got " + width + "x" + height + ")");
        }
        String base = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (base.length() > 64) base = base.substring(base.length() - 64);
        if (!base.toLowerCase(Locale.ROOT).endsWith(".png")) base += ".png";
        Path destination = CAPES_DIR.resolve(base).normalize();
        if (!destination.getParent().equals(CAPES_DIR.normalize())) throw new IOException("Invalid cape filename");
        Files.copy(source, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        LocalCapeTextures.reload();
        return base;
    }

    public static boolean deleteCape(String filename) {
        try {
            String safeName = Path.of(filename).getFileName().toString();
            Path target = CAPES_DIR.resolve(safeName).normalize();
            if (!target.getParent().equals(CAPES_DIR.normalize())) return false;
            boolean deleted = Files.deleteIfExists(target);
            if (safeName.equals(getActiveCape())) setActiveCape("");
            if (deleted) LocalCapeTextures.reload();
            return deleted;
        } catch (IOException | RuntimeException exception) {
            CosmeticaOffline.LOGGER.error("Could not delete cape", exception);
            return false;
        }
    }

    public static UUID getProfileId() { return profileId; }
    public static Path getRootDirectory() { return ROOT; }
    public static Path getCapesDirectory() { return CAPES_DIR; }
}