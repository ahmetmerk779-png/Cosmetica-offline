package dev.ahmetmerk779.cosmeticaoffline;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Turns PNG files in the local cape folder into GPU textures understood by vanilla's CapeLayer.
 * All registration and releasing is performed on the render thread.
 */
public final class LocalCapeTextures {
    private static final Map<String, Identifier> TEXTURES = new HashMap<>();
    private static final String NAMESPACE = "cosmetica_offline";

    private LocalCapeTextures() {}

    public static void reload() {
        if (!RenderSystem.isOnRenderThread()) {
            Minecraft.getInstance().execute(LocalCapeTextures::reload);
            return;
        }

        TextureManager manager = Minecraft.getInstance().getTextureManager();
        for (Identifier id : TEXTURES.values()) {
            manager.release(id);
        }
        TEXTURES.clear();

        try (var files = Files.list(LocalCosmetics.getCapesDirectory())) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".png"))
                    .sorted()
                    .forEach(path -> register(manager, path));
        } catch (IOException exception) {
            CosmeticaOffline.LOGGER.error("Could not scan local cape folder", exception);
        }
    }

    private static void register(TextureManager manager, Path path) {
        String filename = path.getFileName().toString();
        try (InputStream input = Files.newInputStream(path)) {
            NativeImage image = NativeImage.read(input);
            if (image.getWidth() != 64 || (image.getHeight() != 32 && image.getHeight() != 64)) {
                CosmeticaOffline.LOGGER.warn("Skipping cape {}: unsupported size {}x{}",
                        filename, image.getWidth(), image.getHeight());
                image.close();
                return;
            }

            String safe = filename.substring(0, filename.length() - 4)
                    .toLowerCase().replaceAll("[^a-z0-9_.-]", "_");
            Identifier id = Identifier.fromNamespaceAndPath(NAMESPACE, "local/" + safe);
            manager.register(id, new DynamicTexture(id::toString, image));
            TEXTURES.put(filename, id);
            CosmeticaOffline.LOGGER.info("Loaded local cape texture: {}", filename);
        } catch (IOException | RuntimeException exception) {
            CosmeticaOffline.LOGGER.warn("Could not load local cape {}", filename, exception);
        }
    }

    public static ClientAsset.ResourceTexture get(String filename) {
        Identifier id = TEXTURES.get(filename);
        return id == null ? null : new ClientAsset.ResourceTexture(id, id);
    }
}
