package dev.ahmetmerk779.cosmeticaoffline;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.nio.file.Path;
import java.util.List;

public final class WardrobeScreen extends Screen {
    private final Screen parent;
    private EditBox pathField;
    private String status = "Cape PNG dosyasini sec veya capes klasorune kopyala.";
    private int page;

    public WardrobeScreen(Screen parent) {
        super(Component.literal("Cosmetica Offline"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        pathField = new EditBox(this.font, center - 150, this.height - 62, 235, 20, Component.literal("PNG dosya yolu"));
        pathField.setMaxLength(512);
        pathField.setHint(Component.literal("/storage/emulated/0/Download/cape.png"));
        addRenderableWidget(pathField);
        addRenderableWidget(Button.builder(Component.literal("ICE AKTAR"), button -> importFromPath())
                .bounds(center + 92, this.height - 63, 76, 22).build());
        addRenderableWidget(Button.builder(Component.literal("KAPAT"), button -> onClose())
                .bounds(center - 45, this.height - 31, 90, 20).build());
    }

    private void importFromPath() {
        String raw = pathField.getValue().trim();
        if (raw.isEmpty()) {
            status = "Once PNG dosyasinin tam yolunu yaz.";
            return;
        }
        try {
            String imported = LocalCosmetics.importCape(Path.of(raw));
            LocalCosmetics.setActiveCape(imported);
            status = imported + " eklendi ve secildi.";
            page = 0;
        } catch (Exception exception) {
            status = "Aktarma hatasi: " + exception.getMessage();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(0, 0, this.width, this.height, 0xE9101219);
        int center = this.width / 2;
        graphics.fill(center - 178, 18, center + 178, this.height - 16, 0xF51B1F2A);
        graphics.fill(center - 178, 18, center + 178, 54, 0xFF252B3A);
        graphics.drawCenteredString(this.font, "COSMETICA  /  OFFLINE WARDROBE", center, 31, 0xFFFFFFFF);
        graphics.drawString(this.font, "Yalnizca bu cihazda saklanir - Sunucuya gonderilmez",
                center - 160, 65, 0xFF9BA8BD, false);

        List<String> capes = LocalCosmetics.getCapeFiles();
        String active = LocalCosmetics.getActiveCape();
        graphics.drawString(this.font, "KAYITLI CAPELER (" + capes.size() + ")", center - 160, 83, 0xFF70C7FF, false);
        int pageSize = 5;
        int pages = Math.max(1, (capes.size() + pageSize - 1) / pageSize);
        page = Math.max(0, Math.min(page, pages - 1));
        int start = page * pageSize;
        for (int i = start; i < Math.min(capes.size(), start + pageSize); i++) {
            String cape = capes.get(i);
            int y = 101 + (i - start) * 29;
            boolean selected = cape.equals(active);
            graphics.fill(center - 162, y - 4, center + 162, y + 21, selected ? 0xFF244C54 : 0xFF292F3D);
            graphics.drawString(this.font, selected ? "✓ " + cape : cape, center - 151, y + 4,
                    selected ? 0xFF8DF0D0 : 0xFFE2E7F0, false);
            graphics.drawString(this.font, "SEC", center + 130, y + 4, 0xFF70C7FF, false);
        }
        if (capes.isEmpty()) {
            graphics.drawCenteredString(this.font, "Henuz cape yok. PNG dosyani asagidan aktar.", center, 119, 0xFFB4BDCC);
        }
        graphics.drawString(this.font, "Sayfa " + (page + 1) + " / " + pages + "  -  Bir cape satirina dokunarak sec.",
                center - 160, 253, 0xFF9BA8BD, false);
        graphics.drawString(this.font, "Durum: " + status, center - 160, this.height - 87, 0xFFFFD27A, false);
        graphics.drawString(this.font, "Klasor: config/cosmetica-offline/capes/", center - 160, this.height - 75, 0xFF7F8CA3, false);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;
        if (button != 0) return false;
        int center = this.width / 2;
        if (mouseX >= center - 162 && mouseX <= center + 162 && mouseY >= 97 && mouseY < 242) {
            int row = (int) ((mouseY - 97) / 29);
            int index = page * 5 + row;
            List<String> capes = LocalCosmetics.getCapeFiles();
            if (index >= 0 && index < capes.size()) {
                String filename = capes.get(index);
                LocalCosmetics.setActiveCape(filename);
                status = filename + " secildi.";
                return true;
            }
        }
        if (mouseY >= 245 && mouseY <= 270) {
            if (mouseX < center) page = Math.max(0, page - 1);
            else page = Math.min(Math.max(0, (LocalCosmetics.getCapeFiles().size() - 1) / 5), page + 1);
            return true;
        }
        return false;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }
}