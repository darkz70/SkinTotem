# SkinTotem v2

Fabric mod for Minecraft **1.21.1** that replaces the Totem of Undying texture with a mini-player figure generated from a real skin — just like [skinmc.net/totem](https://skinmc.net/totem), but live in-game, without uploading any PNG files.

---

## Features

- **No PNG uploading.** Skins are fetched automatically from:
  - Mojang (official accounts)
  - Ely.by (cracked/alternative accounts)
  - TLauncher
  - Any direct URL to a skin PNG
- **Two ways to set a skin:**
  1. **Rename the Totem on an anvil** — type a player nickname or a skin URL as the name
  2. **Use the command** `/totem <nick|url>` while holding the Totem
- **LRU cache** — skins are cached per session so the same player is only fetched once
- **Flat 2D layout** matching skinmc.net/totem — head, body, arms, legs
- **Compatible with** FCL / PojavLauncher (Android)

---

## Usage

### Method 1: Anvil rename

1. Put a **Totem of Undying** in an anvil
2. In the name field, type either:
   - A player nickname: `Notch`
   - A direct skin URL: `https://example.com/skin.png`
3. Take the renamed totem — the texture will update automatically

### Method 2: Command

Hold a Totem of Undying in your main hand and run:

```
/totem Notch
/totem https://example.com/skin.png
```

The totem in your hand will be renamed to that value and the skin will load.

---

## How it works

1. The item's custom name stores the nickname or URL
2. On the client side, when the totem is about to be rendered, the mod reads the name
3. It fetches the skin (Mojang API → Ely.by → TLauncher → direct URL)
4. `TotemTextureGenerator` assembles a 16×16 flat player figure from the 64×64 skin sheet
5. The texture is registered as a dynamic `NativeImageBackedTexture` and applied to the totem

---

## Building

```bash
./gradlew build
```

The JAR will be in `build/libs/`.

---

## Credits

- Original concept: [Pawelgit1234/skintotem](https://github.com/Pawelgit1234/skintotem)
- Fork & rewrite: [darkz70](https://github.com/darkz70)
