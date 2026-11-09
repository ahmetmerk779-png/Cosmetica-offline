# Cosmetica Offline

A client-side Fabric cosmetics project for Minecraft **1.21.11**.

## Goals

- Works with offline/cracked launcher profiles; no Microsoft/Mojang login check.
- Cosmetics are intended to render locally for the user only.
- No cosmetics backend, account linking, telemetry, or server modification.
- Android ARM64 / PojavLauncher-friendly design: avoid native libraries and heavyweight dependencies.
- Local cape files and settings live in `config/cosmetica-offline/`.

## Build

Requirements: JDK 21 and internet access for Gradle dependency resolution.

```bash
./gradlew build
```

The mod JAR will be in `build/libs/`.

## Local data

On first launch the mod creates:

- `config/cosmetica-offline/profile-id.txt`
- `config/cosmetica-offline/capes/`
- `config/cosmetica-offline/HOW_TO_ADD_CAPE.txt`

The initial commit establishes the Fabric client project and persistent offline profile storage. Cape rendering and the in-game wardrobe are the next implementation stage; this initial commit is not yet a finished playable cosmetics release.

## Privacy / multiplayer behavior

This project is designed to affect only the local client's rendering. It does not send cosmetic data to servers or other players. Other players will not see these cosmetics.

## Compatibility

Target: Minecraft 1.21.11, Fabric Loader 0.20.x, Java 21.
