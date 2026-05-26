# SkinTotem v2.3 — Indigo Fix

[🇷🇺 Русский](#русский) | [🇺🇸 English](#english)

---

<a name="русский"></a>
## 🇷🇺 Русский

Fabric мод для Minecraft **1.21.1**.  
Заменяет текстуру Тотема Бессмертия на фигурку из скина игрока — как на [skinmc.net/totem](https://skinmc.net/totem).

### Что нового в v2.3 (Indigo Fix)
*   **Исправлен Indigo**: Исправлена совместимость с рендерером Indigo (используется в Sodium).
*   **Новый рендеринг**: Переход на `BuiltinItemRendererRegistry` для более стабильного отображения.
*   **Оптимизация**: Улучшено кеширование скинов и работа с ресурсами.

### Как использовать
*   **Наковальня**: Переименуй тотем → напиши ник или ссылку на скин.
*   **Команда**: Держи тотем в руке и пиши:
    ```
    /totem Notch
    /totem https://example.com/skin.png
    ```

### Источники скинов
1. **Mojang** (Лицензия)
2. **Ely.by**
3. **TLauncher**
4. Прямая **URL-ссылка**

---

<a name="english"></a>
## 🇺🇸 English

Fabric mod for Minecraft **1.21.1**.  
Replaces the Totem of Undying texture with a figure based on the player's skin — similar to [skinmc.net/totem](https://skinmc.net/totem).

### What's New in v2.3 (Indigo Fix)
*   **Indigo Fix**: Fixed compatibility with the Indigo renderer (used by Sodium).
*   **New Rendering**: Switched to `BuiltinItemRendererRegistry` for more stable display.
*   **Optimization**: Improved skin caching and resource handling.

### How to Use
*   **Anvil**: Rename the totem → enter a player's nickname or a direct skin link.
*   **Command**: Hold the totem in your hand and type:
    ```
    /totem Notch
    /totem https://example.com/skin.png
    ```

### Skin Sources
1. **Mojang** (Premium)
2. **Ely.by**
3. **TLauncher**
4. Direct **URL link**

---

## Building / Сборка

```bash
./gradlew build
```
The JAR file will be in `build/libs/`. / JAR файл появится в `build/libs/`.
