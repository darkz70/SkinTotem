package com.darkz.skintotem.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
//? if >=1.20.5 {
import net.minecraft.component.DataComponentTypes;
//?}
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import com.darkz.skintotem.api.MojangAPI;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class SkinTotemCommand {

    private static final String P = "§6[SkinTotem]§r ";

    public static LiteralArgumentBuilder<FabricClientCommandSource> getInfoCommand() {
        return literal("info").executes(ctx -> {
            ctx.getSource().sendFeedback(Text.literal(
                P + "§bv1.0.0 §8| §bAuthor: §fDarkz §8| §fK-TEAM"
            ));
            return 1;
        });
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCreditsCommand() {
        return literal("credits").executes(ctx -> {
            ctx.getSource().sendFeedback(Text.literal(
                "\n§6╔═══════════════════════════════════╗\n" +
                "§6║  §bSkinTotem §fv1.0.0               §6║\n" +
                "§6║  §7Author:       §fDarkz           §6║\n" +
                "§6║  §7Team:         §fK-TEAM          §6║\n" +
                "§6╚═══════════════════════════════════╝\n"
            ));
            return 1;
        });
    }

    // Реальный формат провайдеров: короткие префиксы "#"/"@"/"url:", раскрываются
    // в TagsSkinProviders.expandShorthand() в "TLauncher|..." / "ElyBy|..." / "Url|...".
    public static LiteralArgumentBuilder<FabricClientCommandSource> getTlCommand() {
        return literal("tl")
            .then(argument("nickname", StringArgumentType.word())
                .executes(ctx -> renameHeldTotem(ctx, "#" + StringArgumentType.getString(ctx, "nickname"))));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getElyCommand() {
        return literal("ely")
            .then(argument("nickname", StringArgumentType.word())
                .executes(ctx -> renameHeldTotem(ctx, "@" + StringArgumentType.getString(ctx, "nickname"))));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getUrlCommand() {
        return literal("url")
            .then(argument("link", StringArgumentType.greedyString())
                .executes(ctx -> renameHeldTotem(ctx, "url:" + StringArgumentType.getString(ctx, "link"))));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getMojangCommand() {
        return literal("mojang").executes(ctx -> {
            MojangAPI.useFallbackAPI = true;
            ctx.getSource().sendFeedback(Text.literal(P + "§aMojang refresh enabled (using fallback API)"));
            return 1;
        });
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getModelCommand() {
        return literal("model")
            .then(argument("model_id", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    String[] models = {"2d_doll", "3d_doll", "3d_funko", "gnom", "mini_3d", "parrot", "player_bucket", "pots", "rat", "stairs", "wheelchair"};
                    for (String model : models) builder.suggest(model);
                    return builder.buildFuture();
                })
                .executes(ctx -> {
                    String modelId = StringArgumentType.getString(ctx, "model_id");
                    com.darkz.skintotem.config.SkinTotemModConfig.getInstance().setStandardTotemDollModelValue(com.darkz.skintotem.SkinTotemMod.id("dolls/" + modelId + ".bbmodel"));
                    com.darkz.skintotem.config.SkinTotemModConfig.getInstance().save();
                    ctx.getSource().sendFeedback(Text.literal(P + "§aDefault model set to: §f" + modelId));
                    return 1;
                }));
    }

    private static int renameHeldTotem(com.mojang.brigadier.context.CommandContext<FabricClientCommandSource> ctx, String name) {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        if (player == null) return 0;

        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty() || !stack.isOf(Items.TOTEM_OF_UNDYING)) {
            ctx.getSource().sendError(Text.literal("§cHold a Totem of Undying in your main hand!"));
            return 0;
        }

        //? if >=1.20.5 {
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(name));
        //?} else {
        /*stack.setCustomName(Text.literal(name));
        *///?}

        ctx.getSource().sendFeedback(Text.literal(P + "§aSkin totem set to: §f" + name));
        return 1;
    }

    public static com.mojang.brigadier.Command<FabricClientCommandSource> getHelpExecutor() {
        return ctx -> {
            ctx.getSource().sendFeedback(Text.literal(
                P + "§7Commands:\n" +
                "  §f/skintotem info\n" +
                "  §f/skintotem refresh\n" +
                "  §f/skintotem tl <nickname>\n" +
                "  §f/skintotem ely <nickname>\n" +
                "  §f/skintotem url <link>\n" +
                "  §f/skintotem mojang\n" +
                "  §f/skintotem model <model_id>\n" +
                "  §f/skintotem credits"
            ));
            return 1;
        };
    }
}
