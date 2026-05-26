# SkinTotem v2.4 — No Mixin

[🇷🇺 Русский](#русский) | [🇺🇸 English](#english)

---

<a name="русский"></a>
## 🇷🇺 Русский

Fabric мод для Minecraft **1.21.1**.  
Заменяет текстуру Тотема Бессмертия на фигурку из скина игрока — как на [skinmc.net/totem](https://skinmc.net/totem).

### Что нового в v2.4 (No Mixin)
*   **Отказ от Mixin**: Полностью удалены Mixin-трансформации для повышения стабильности и совместимости.
*   **Чистый рендеринг**: Использование стандартных API Fabric для рендеринга предметов.
*   **Максимальная совместимость**: Теперь мод не конфликтует с другими модификациями, изменяющими рендеринг (Sodium, Iris и др.).

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

### What's New in v2.4 (No Mixin)
*   **No Mixin**: Completely removed Mixin transformations for better stability and compatibility.
*   **Clean Rendering**: Uses standard Fabric APIs for item rendering.
*   **Maximum Compatibility**: No more conflicts with other rendering mods (Sodium, Iris, etc.).

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
