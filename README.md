# SkinTotem v2.1 — Fixed

Fabric мод для Minecraft **1.21.1**.  
Заменяет текстуру Тотема Бессмертия на фигурку из скина игрока — как на [skinmc.net/totem](https://skinmc.net/totem).

---

## Что исправлено в v2.1

| Проблема | Решение |
|----------|---------|
| Краш при загрузке (mixin failed) | Убраны `@ModifyVariable` на `renderItem` — заменены безопасными `@Inject` и `@ModifyArg` |
| `skintotem.mixins.json` (пустой серверный конфиг) | Убран, остался только `skintotem.client.mixins.json` с `environment: client` |
| Дублирующийся запрос скина | Thread-local + Set loading предотвращают повторные запросы |

---

## Как использовать

### Наковальня
Переименуй тотем → напиши ник или ссылку на скин.

### Команда
Держи тотем в руке, пиши:
```
/totem Notch
/totem https://example.com/skin.png
```

---

## Источники скинов (автоматически)
1. **Mojang** (лицензионные аккаунты)
2. **Ely.by** (пиратские / альтернативные)
3. **TLauncher**
4. Прямая **URL-ссылка** на PNG

---

## Сборка

```bash
./gradlew build
```

JAR появится в `build/libs/`.

---

## Технические детали

- Миксины только клиентские (`environment: client`) — нет краша при запуске на сервере
- `SkinTotemTextureManager` регистрирует `NativeImageBackedTexture` через Minecraft texture manager
- `TotemRenderLayerMixin` меняет `RenderLayer` через `@ModifyArg` — не трогает сигнатуру метода
- `TotemModelMixin` оборачивает `BakedModel` в `SkinTotemBakedModel` с нужным texture ID
- Скин грузится асинхронно — первый рендер стандартный, потом автоматически обновляется
