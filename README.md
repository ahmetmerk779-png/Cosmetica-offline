# Cosmetica Offline

Minecraft **1.21.11 / Fabric** için istemci taraflı, çevrimdışı kozmetik projesi.

## Şu anki özellikler

- Microsoft/Mojang hesabı veya Cosmetica sunucusu gerektirmez.
- Yerel profil kimliği ve kozmetik ayarlarını `config/cosmetica-offline/` altında saklar.
- Ana menü ve oyun duraklatma ekranına **COSMETICA** düğmesi ekler.
- Modern koyu temalı, dokunmatik ekrana uygun yerel cape dolabı.
- PNG cape dosyalarını tam dosya yolundan içe aktarır.
- 64x32 ve 64x64 PNG boyutlarını doğrular.
- Cape listesini gösterir ve aktif cape seçimini diske kaydeder.
- Ağ isteği yapmaz; dosyaları hiçbir sunucuya göndermez.

## Cape ekleme

1. PNG cape dosyanı telefonunun dosya yöneticisinde bul.
2. Oyunda ana menüdeki veya ESC menüsündeki **COSMETICA** düğmesine dokun.
3. Dosyanın tam yolunu kutuya yaz. Örnek: `/storage/emulated/0/Download/cape.png`
4. **İÇE AKTAR** düğmesine bas ve listeden cape'ini seç.

Alternatif: PNG dosyasını doğrudan `config/cosmetica-offline/capes/` klasörüne kopyala. Dolap ekranını yeniden aç.

## Önemli durum

Dolap, PNG içe aktarma, yerel listeleme ve seçim kaydı kaynak koda eklendi. **Aktif cape'in oyuncu modelinde 3D olarak çizilmesi henüz tamamlanmadı.** Bu nedenle şu an cape seçimi kaydedilir ama karakterinin sırtında görünmesi beklenmemelidir. Derleme ve PojavLauncher üzerinde çalışma testi de henüz doğrulanmadı.

## Derleme

JDK 21 ve internet bağlantısı gerekir:

```bash
gradle build
```

GitHub Actions her push'ta derleme işini otomatik çalıştırır ve başarılı derlemede JAR dosyasını artifact olarak yükler.

## Uyumluluk hedefi

- Minecraft 1.21.11
- Fabric Loader 0.20.x
- Java 21
- PojavLauncher / Android ARM64 hedeflenir; gerçek cihaz testi henüz yapılmadı.

## Gizlilik

Mod kozmetik verilerini sunucuya göndermez. Seçilen cape yalnızca bu istemcideki yerel ayar olarak saklanır. Diğer oyuncular cape'i göremez.
