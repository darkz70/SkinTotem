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
import com.darkz.skintotem.SkinTotemMod;
// import ru.kteam.skintotem.render.ModelVariant;
// import ru.kteam.skintotem.render.ModelCache;
// import ru.kteam.skintotem.util.SkinCache;

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
    /* private static final SuggestionProvider<FabricClientCommandSource> MODEL_SUGGESTIONS =
            (ctx, builder) -> {
                for (ModelVariant v : ModelVariant.values())
                    builder.suggest(v.id, Text.literal(v.displayName));
                return builder.buildFuture();
            }; */

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            ClientCommandManager.literal("skintotem")

                // /skintotem info
                .then(ClientCommandManager.literal("info").executes(ctx -> {
                    // String variant = SkinTotemMod.CONFIG.defaultVariant;
                    String variant = "unknown";
                    ctx.getSource().sendFeedback(Text.literal(
                        P + "§bv1.0.0  §8|  §bАвтор: §fDarkz §8| §fK-TEAM"
                    ));
                    return 1;
                }))

                // /skintotem refresh [all | <ник>]
                .then(ClientCommandManager.literal("refresh")
                    .executes(ctx -> {
                        ctx.getSource().sendFeedback(Text.literal(P + "§aRefresh not implemented in this version"));
                        return 1;
                    }))

                // /skintotem model <variant>
                // .then(ClientCommandManager.literal("model")...)

                // /skintotem models
                // .then(ClientCommandManager.literal("models")...)

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
                        // /skintotem tl
.then(ClientCommandManager.literal("tl").executes(ctx -> {
    SkinTotemMod.CONFIG.skinUrl =
            "https://auth.tlauncher.org/skin/profile/texture/login/%player%";
    SkinTotemMod.CONFIG.save();

    ctx.getSource().sendFeedback(
            Text.literal(P + "§aSkin source switched to §eTLauncher"));
    return 1;
}))

// /skintotem ely
.then(ClientCommandManager.literal("ely").executes(ctx -> {
    SkinTotemMod.CONFIG.skinUrl =
            "https://skinsystem.ely.by/skins/%player%.png";
    SkinTotemMod.CONFIG.save();

    ctx.getSource().sendFeedback(
            Text.literal(P + "§aSkin source switched to §bEly.by"));
    return 1;
}))

// /skintotem url <url>
.then(ClientCommandManager.literal("url")
    .then(ClientCommandManager.argument("url", StringArgumentType.greedyString())
        .executes(ctx -> {

            String url = StringArgumentType.getString(ctx, "url");

            SkinTotemMod.CONFIG.skinUrl = url;
            SkinTotemMod.CONFIG.save();

            ctx.getSource().sendFeedback(
                    Text.literal(P + "§aCustom skin URL set to:\n§f" + url));

            return 1;
        })))

        SkinTotemMod.LOGGER.info("[SkinTotem] Команды зарегистрированы");
    }
}
