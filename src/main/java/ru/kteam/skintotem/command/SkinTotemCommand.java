package ru.kteam.skintotem.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.kteam.skintotem.SkinTotemMod;
import ru.kteam.skintotem.render.ModelVariant;
import ru.kteam.skintotem.render.ModelCache;
import ru.kteam.skintotem.util.SkinCache;

/**
 * Клиентские команды:
 *   /skintotem                       — помощь
 *   /skintotem info                  — версия, кэш
 *   /skintotem refresh [ник|all]     — сброс кэша скинов
 *   /skintotem model <variant>       — сменить модель по умолчанию
 *   /skintotem models                — список всех моделей
 *   /skintotem credits               — кредиты K-TEAM
 */
@Environment(EnvType.CLIENT)
public class SkinTotemCommand {

    private static final String P = "§6[SkinTotem]§r ";

    // Подсказки для аргумента модели
    private static final SuggestionProvider<FabricClientCommandSource> MODEL_SUGGESTIONS =
            (ctx, builder) -> {
                for (ModelVariant v : ModelVariant.values())
                    builder.suggest(v.id, Text.literal(v.displayName));
                return builder.buildFuture();
            };

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            ClientCommandManager.literal("skintotem")

                // /skintotem info
                .then(ClientCommandManager.literal("info").executes(ctx -> {
                    String variant = SkinTotemMod.CONFIG.defaultVariant;
                    ctx.getSource().sendFeedback(Text.literal(
                        P + "§bv1.0.0  §8|  §bСкин-кэш: §f" + SkinCache.getCacheSize() +
                        "\n" + P + "§bМодель: §f" + variant +
                        "  §8|  §bАвтор: §fDarkz §8| §fK-TEAM"
                    ));
                    return 1;
                }))

                // /skintotem refresh [all | <ник>]
                .then(ClientCommandManager.literal("refresh")
                    .then(ClientCommandManager.literal("all").executes(ctx -> {
                        SkinCache.invalidateAll();
                        ctx.getSource().sendFeedback(Text.literal(P + "§aВесь кэш скинов сброшен!"));
                        return 1;
                    }))
                    .then(ClientCommandManager.argument("nickname", StringArgumentType.word())
                        .executes(ctx -> {
                            String nick = StringArgumentType.getString(ctx, "nickname");
                            SkinCache.invalidate(nick);
                            SkinCache.getSkin(nick);
                            ctx.getSource().sendFeedback(Text.literal(P + "§aОбновляю скин: §f" + nick));
                            return 1;
                        }))
                    .executes(ctx -> {
                        MinecraftClient mc = MinecraftClient.getInstance();
                        if (mc.player == null) return 0;
                        String nick = mc.player.getName().getString();
                        SkinCache.invalidate(nick);
                        SkinCache.getSkin(nick);
                        ctx.getSource().sendFeedback(Text.literal(P + "§aОбновляю скин: §f" + nick));
                        return 1;
                    }))

                // /skintotem model <variant>
                .then(ClientCommandManager.literal("model")
                    .then(ClientCommandManager.argument("variant", StringArgumentType.word())
                        .suggests(MODEL_SUGGESTIONS)
                        .executes(ctx -> {
                            String id = StringArgumentType.getString(ctx, "variant");
                            ModelVariant v = ModelVariant.fromId(id);
                            SkinTotemMod.CONFIG.defaultVariant = v.id;
                            SkinTotemMod.CONFIG.save();
                            ctx.getSource().sendFeedback(Text.literal(
                                P + "§aМодель изменена: §f" + v.displayName + " §7(" + v.id + ")"
                            ));
                            return 1;
                        })))

                // /skintotem models
                .then(ClientCommandManager.literal("models").executes(ctx -> {
                    StringBuilder sb = new StringBuilder(P + "§bДоступные модели:\n");
                    for (ModelVariant v : ModelVariant.values()) {
                        boolean active = v.id.equals(SkinTotemMod.CONFIG.defaultVariant);
                        sb.append(active ? "§a▶ " : "§7  ")
                          .append("§f").append(v.id)
                          .append(" §8— §7").append(v.displayName)
                          .append("\n");
                    }
                    ctx.getSource().sendFeedback(Text.literal(sb.toString()));
                    return 1;
                }))

                // /skintotem credits
                .then(ClientCommandManager.literal("credits").executes(ctx -> {
                    ctx.getSource().sendFeedback(Text.literal(
                        "\n§6╔═══════════════════════════════════╗\n" +
                        "§6║  §bSkinTotem §fv1.0.0               §6║\n" +
                        "§6║  §7Автор:         §fDarkz, §fKlashRaick §6║\n" +
                        "§6║  §7Команда:       §fK-TEAM             §6║\n" +
                        "§6║  §7Благодарность: §fKlashRaick §c♥     §6║\n" +
                        "§6╚═══════════════════════════════════╝\n"
                    ));
                    return 1;
                }))

                // /skintotem — помощь
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(Text.literal(
                        P + "§7Команды:\n" +
                        "  §f/skintotem info\n" +
                        "  §f/skintotem refresh §7[ник | all]\n" +
                        "  §f/skintotem model §7<вариант>\n" +
                        "  §f/skintotem models\n" +
                        "  §f/skintotem credits"
                    ));
                    return 1;
                })
        );

        SkinTotemMod.LOGGER.info("[SkinTotem] Команды зарегистрированы");
    }
}
