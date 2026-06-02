<img src="src/main/resources/icon/icon.png" align="right" width="130px" alt="mod logo"/>

</div align="center">
A Fabric Mod for Minecraft 1.20.1

Replaces the Totem of Undying with a 3D doll using your Minecraft skin

""Minecraft" (https://img.shields.io/badge/Minecraft-1.20.1-green?style=for-the-badge)" (https://minecraft.net)
""Fabric" (https://img.shields.io/badge/Loader-Fabric-blue?style=for-the-badge)" (https://fabricmc.net)
""Version" (https://img.shields.io/badge/Version-1.0.0-orange?style=for-the-badge)" (https://github.com/darkz70/SkinTotem)

</div>---

✨ Features

Feature| Description
🎭 Automatic Skin Loading| Fetches player skins directly from the official Mojang API
💾 Caching| Skins are cached for 10 minutes to reduce API requests
🎨 Slim / Classic Support| Supports both Alex and Steve player models
🌐 Multiplayer Compatible| Works on any server without requiring a server-side mod
⚙️ Configurable| Fully configurable through ModMenu + Cloth Config
🔧 NBT Customization| Customize individual totems via an anvil
🎬 Activation Animation| Smooth and immersive Totem activation animation

---

📦 Installation

1. Install Fabric Loader for Minecraft 1.20.1
2. Install Fabric API
3. Download "skintotem-1.0.0.jar" and place it in your "mods/" folder
4. Optional: Install ModMenu and Cloth Config for an in-game configuration GUI

---

🎮 Usage

Automatic Mode

Simply hold a Totem of Undying in your hand, and it will automatically display your skin.

Custom Player Skin (Using an Anvil)

1. Place a Totem of Undying into an anvil
2. Rename it to any Minecraft username
3. The totem will display that player's skin

Commands

/skintotem                    - Show help
/skintotem info               - Display version and cache information
/skintotem refresh            - Refresh the current player's skin
/skintotem refresh <player>   - Refresh a specific player's skin
/skintotem refresh all        - Clear the entire skin cache
/skintotem credits            - Show credits

---

⚙️ Configuration (ModMenu)

Setting| Default| Description
Use Current Player Skin| ✅| Automatically use your own skin
Default Username| —| Used when automatic skin loading is disabled
Render in First Person| ✅| Display the totem in first-person view
Show Cape| ✅| Render the player's cape
Scale| 1.0| Doll size (0.5–2.0)
Y Rotation| 0°| Rotation angle of the doll
Activation Animation| ✅| Play animation when the totem is activated

---

📋 Dependencies

Mod| Required| Description
Fabric API| ✅| Core Fabric API dependency
ModMenu| ❌| Adds a settings button to the mod list
Cloth Config| ❌| Configuration GUI library

---

👥 Credits

<div align="center">Role| Contributor
👨‍💻 Mod Author| Darkz KlashRaick
🏆 Team| K-TEAM
💛 Special Thanks| KlashRaick

Inspired by the SkinTotem and My-Totem-Doll projects.

</div>---

📄 License

Released under the MIT License — free to use, modify, and distribute with proper attribution.

---

<div align="center">Made with ❤️ by Darkz | K-TEAM

</div>

