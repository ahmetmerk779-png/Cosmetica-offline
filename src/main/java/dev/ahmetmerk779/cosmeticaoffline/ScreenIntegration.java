package dev.ahmetmerk779.cosmeticaoffline;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public final class ScreenIntegration {
    private ScreenIntegration() {}

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof TitleScreen) && !(screen instanceof PauseScreen)) return;
            int x = Math.max(4, screen.width - 104);
            Screens.getButtons(screen).add(Button.builder(Component.literal("COSMETICA"), button ->
                    client.setScreen(new WardrobeScreen(screen)))
                    .bounds(x, 8, 96, 20).build());
        });
    }
}