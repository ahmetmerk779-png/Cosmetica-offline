package dev.ahmetmerk779.cosmeticaoffline;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.PlayerSkin;

import java.util.Optional;

/**
 * Patches only the local player's render-state skin. The normal vanilla CapeLayer then draws
 * the cape with Minecraft's own cloth movement, walking sway, elytra rules and lighting.
 */
public final class LocalCapeRenderer {
    private static PlayerSkin cachedSource;
    private static String cachedFilename = "";
    private static PlayerSkin cachedPatched;

    private LocalCapeRenderer() {}

    public static void apply(Avatar avatar, AvatarRenderState state) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || avatar != client.player) return;

        String filename = LocalCosmetics.getActiveCape();
        if (filename.isBlank()) return;

        PlayerSkin source = state.skin;
        if (source == null) return;

        var texture = LocalCapeTextures.get(filename);
        if (texture == null) return;

        if (source == cachedSource && filename.equals(cachedFilename) && cachedPatched != null) {
            state.skin = cachedPatched;
        } else {
            PlayerSkin patched = source.with(new PlayerSkin.Patch(
                    Optional.empty(),
                    Optional.of(texture),
                    Optional.empty(),
                    Optional.empty()
            ));
            cachedSource = source;
            cachedFilename = filename;
            cachedPatched = patched;
            state.skin = patched;
        }

        // A local cosmetic should still display if the cape body part was hidden in Skin Customization.
        state.showCape = true;
    }

    public static void clearCache() {
        cachedSource = null;
        cachedFilename = "";
        cachedPatched = null;
    }
}
