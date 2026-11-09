package dev.ahmetmerk779.cosmeticaoffline.mixin;

import dev.ahmetmerk779.cosmeticaoffline.LocalCapeRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Supplies the local cape texture to vanilla's existing 3D cape renderer. */
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {
    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At("TAIL")
    )
    private void cosmeticaOffline$applyCape(Avatar avatar, AvatarRenderState state, float partialTick, CallbackInfo ci) {
        LocalCapeRenderer.apply(avatar, state);
    }
}
