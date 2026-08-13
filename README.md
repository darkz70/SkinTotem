<div align="center">

<img src="src/main/resources/icon/icon.png" width="130px" alt="mod logo"/>

# SkinTotem

Replaces the Totem of Undying with a 3D doll using your Minecraft skin

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green?style=for-the-badge)
[![Forge](https://img.shields.io/badge/Loader-Forge-orange?style=for-the-badge)](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html)
![Version](https://img.shields.io/badge/Version-2.0.1-orange?style=for-the-badge)

</div>

---

## 📥 Link

<div align="center">

[![CurseForge Link-Banner](https://cdn.modrinth.com/data/cached_images/e867d37a2f6ad224258b75aacf6477e777427717.png)](https://www.curseforge.com/minecraft/mc-mods/skin-totem)
[![GitHub Link-Banner](https://cdn.modrinth.com/data/cached_images/ae65154a7b076cd508f14975a27d1e75e3449a1d.png)](https://github.com/darkz70/SkinTotem)
[![rinth Link-Banner](https://cdn.modrinth.com/data/cached_images/b9c43eaea7fc523285ae0981829b84e206672b48.png)](https://modrinth.com/mod/skin-totem)
<a href="https://t.me/darkz42i69k">
  <img src="https://i.ibb.co/LhRwY0DQ/images.png" width="48">
</a>

</div>

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🎭 Automatic Skin Loading | Fetches player skins directly from the official Mojang API |
| 💾 Caching | Skins are cached for 10 minutes to reduce API requests |
| 🎨 Slim / Classic Support | Supports both Alex and Steve player models |
| 🌐 Multiplayer Compatible | Works on any server without requiring a server-side mod |
| ⚙️ Configurable | Fully configurable through Menu + Cloth Config |
| 🔧 NBT Customization | Customize individual totems via an anvil |
| 🎬 Activation Animation | Smooth and immersive totem activation animation |

---

## 📦 Installation

1. Install [Minecraft Forge 1.20.1](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html) — the official Forge loader and installer
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download `skintotem-1.1.1.jar` and place it in your `mods/` folder
4. **Optional:** Install [Menu](https://modrinth.com/mod/modmenu) and [Cloth Config](https://modrinth.com/mod/cloth-config) for an in-game configuration GUI

---

## 🎮 Usage

### Automatic e

Simply hold a Totem of Undying in your hand — it will automatically display your skin.

### Custom Skin (via Anvil)

Place a Totem of Undying into an anvil and rename it using one of the formats below:

| Format | Provider | Example |
|--------|----------|---------|
| `Notch` | Mojang | `Notch` |
| `#Notch` | TLauncher | `#Notch` |
| `@Notch` | Ely.by | `@Notch` |
| `NameMC\|Notch` | NameMC | `NameMC\|Notch` |

### Commands (SkinTotem 1.20.1-26.2
```
"/skintotem <nickname>" — load a Mojang skin
"/skintotem tl <nickname>" — load a TLauncher skin
"/skintotem ely <nickname>" — load an Ely.by skin
"/skintotem url <link>" — load a skin from a direct image URL
"/skintotem model <model_id>" — change the doll model
"/skintotem refresh" — refresh the skin cache
"/skintotem credits" — info authors
```
---

## ⚙️ Configuration (Menu)

| Setting | Default | Description |
|---------|---------|-------------|
| Use Current Player Skin | ✅ | Automatically use your own skin |
| Default Username | — | Used when automatic skin loading is disabled |
| Render in First Person | ✅ | Display the totem in first-person view |
| Show Cape | ✅ | Render the player's cape |
| Scale | 1.0 | Doll size (0.5–2.0) |
| Y Rotation | 0° | Rotation angle of the doll |
| Activation Animation | ✅ | Play animation when the totem is activated |

---

## 📋 Dependencies

|  | Required | Description |
|-----|----------|-------------|
| Minecraft Forge | ✅ | Official Forge loader for Minecraft 1.20.1 |
| Menu | ❌ | Adds a settings button to the mod list |
| Cloth Config | ❌ | Configuration GUI library |
| YetAnotherConfigLib (YACL) | ❌ | Alternative configuration GUI library |

---

## 👥 Credits

| Role | Contributor |
|------|-------------|
| 👨‍💻  Author | Darkz |
| 🏆 Team | FigureStoneTeam |

---

<div align="center">

Made with ❤️ by Darkz | FigureStoneTeam

</div>
