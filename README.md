# SkinTotem v2.2 — Fixed

Fabric mod for Minecraft **1.21.1**.  
Replaces the Totem of Undying texture with a figure based on the player's skin — similar to [skinmc.net/totem](https://skinmc.net/totem).

---

## What's New in v2.2

| Improvement | Description |
|-------------|-------------|
| Refactored Structure | Source code split into `main` and `client` source sets for better organization |
| Improved Rendering | Better integration with Minecraft's rendering system via `SkinTotemBakedModel` |
| Stability | Fixed potential crashes and mixin conflicts from previous versions |

---

## How to Use

### Anvil
Rename the totem → enter a player's nickname or a direct link to a skin.

### Command
Hold the totem in your hand and type:
```
/totem Notch
/totem https://example.com/skin.png
```

---

## Skin Sources (Automatic)
1. **Mojang** (Premium accounts)
2. **Ely.by** (Alternative/Pirate skins)
3. **TLauncher**
4. Direct **URL link** to a PNG file

---

## Building

```bash
./gradlew build
```

The JAR file will be generated in `build/libs/`.

---

## Technical Details

- Client-side only mixins (`environment: client`) — prevents server-side crashes.
- `SkinTotemTextureManager` registers `NativeImageBackedTexture` via Minecraft's texture manager.
- `TotemRenderMixin` modifies the rendering layer to apply the custom skin texture.
- `TotemModelMixin` wraps the `BakedModel` in `SkinTotemBakedModel` with the appropriate texture ID.
- Skins are loaded asynchronously — the default texture is rendered first, then automatically updated once the skin is fetched.
