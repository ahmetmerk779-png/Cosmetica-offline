# Cosmetica Offline

Minecraft **1.21.11 / Fabric** için istemci taraflı, çevrimdışı kozmetik projesi.

## Özellikler

- Microsoft/Mojang hesabı veya Cosmetica sunucusu gerektirmez.
- Yerel profil ve cape ayarlarını `config/cosmetica-offline/` altında saklar.
- Ana menü ve duraklatma ekranında **COSMETICA** düğmesi.
- Mobil kullanıma uygun koyu temalı cape dolabı.
- PNG cape dosyalarını dosya yolundan içe aktarma.
- 64x32 ve 64x64 PNG boyut kontrolü.
- Cape seçme ve silme.
- **3D cape render entegrasyonu:** seçili yerel cape, yalnızca yerel oyuncunun render state'ine eklenir; Minecraft'ın vanilla CapeLayer sistemi kumaş hareketini ve animasyonları çizer.
- Dosyalar sunucuya gönderilmez; diğer oyuncuların istemcilerine aktarılmaz.

## Cape ekleme

1. PNG cape dosyanı telefonunun dosya yöneticisinde bul.
2. Oyunda ana menüdeki veya ESC menüsündeki **COSMETICA** düğmesine dokun.
3. Dosyanın tam yolunu yaz (ör. `/storage/emulated/0/Download/cape.png`).
4. **İÇE AKTAR** düğmesine bas; listeye eklenip seçilir.

Alternatif olarak PNG'yi `config/cosmetica-offline/capes/` klasörüne kopyala.

## Önemli

Render entegrasyonu Minecraft 1.21.11'in `AvatarRenderer` render-state aşamasına mixin uygular ve PlayerSkin cape alanını yerel dinamik doku ile değiştirir. Böylece ayrı, düz bir 2D overlay çizmek yerine vanilla'nın 3D cape modeli, hareketi ve animasyonu kullanılır.

Proje GitHub Actions ile derlenir. **Son commit'in CI derlemesi ve PojavLauncher/Android ARM64 cihaz testi tamamlanmadan çalışan JAR garantisi verilmez.** Build sonucu Actions sekmesinde görünür.

## Hedef

- Minecraft 1.21.11
- Fabric Loader 0.20.x
- Fabric API 0.141.x
- Java 21
- PojavLauncher / Android ARM64 (cihaz testi bekliyor)

## Gizlilik

Cape sadece yerel istemcide görüntülenir. Bu mod diğer oyunculara cape göndermeye veya sunucuya müdahale etmeye çalışmaz.
