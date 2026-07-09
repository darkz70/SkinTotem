package com.darkz.skintotem.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider;
import com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;

public class SkinTotemCommand {

    private static final String P = "§6[SkinTotem]§r ";

    public static LiteralArgumentBuilder<FabricClientCommandSource> getInfoCommand() {
        return literal("info").executes(ctx -> {
            ctx.getSource().sendFeedback(Component.literal(
                P + "§bv1.0.0 §8| §bAuthor: §fDarkz §8| §fLopyMine §8| KlashRaick §8| §fK-TEAM"
            ));
            return 1;
        });
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCreditsCommand() {
        return literal("credits").executes(ctx -> {
            ctx.getSource().sendFeedback(Component.literal(
                "\n§6╔══════════════════════════════════════════════════╗\n" +
                "§6║  §bSkinTotem §fv1.0.0                                     §6║\n" +
                "§6║  §7Author:       §fDarkz      §fKlashRaisk   §fLopyMine    §6║\n" +
                "§6║  §7Team:         §fK-TEAM                                 §6║\n" +
                "§6╚════════════════════════════════════════════════════╝\n"
            ));
            return 1;
        });
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getTlCommand() {
        return literal("tl")
            .then(argument("nickname", StringArgumentType.word())
                .executes(ctx -> renameHeldTotem(ctx, TLauncherSkinProvider.PREFIX + StringArgumentType.getString(ctx, "nickname"))));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getElyCommand() {
        return literal("ely")
            .then(argument("nickname", StringArgumentType.word())
                .executes(ctx -> renameHeldTotem(ctx, ElyBySkinProvider.PREFIX + StringArgumentType.getString(ctx, "nickname"))));
    }

    private static int renameHeldTotem(com.mojang.brigadier.context.CommandContext<FabricClientCommandSource> ctx, String name) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return 0;

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty() || !stack.is(Items.TOTEM_OF_UNDYING)) {
            ctx.getSource().sendError(Component.literal("§cHold a Totem of Undying in your main hand!"));
            return 0;
        }

        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        ctx.getSource().sendFeedback(Component.literal(P + "§aSkin totem set to: §f" + name));
        return 1;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getUrlCommand() {
        return literal("url")
            .then(argument("url", StringArgumentType.greedyString())
                .executes(ctx -> {
                    String url = StringArgumentType.getString(ctx, "url");
                    ctx.getSource().sendFeedback(
                        Component.literal(P + "§aCustom skin URL:\n§f" + url)
                    );
                    return 1;
                })
            );
    }

    public static com.mojang.brigadier.Command<FabricClientCommandSource> getHelpExecutor() {
        return ctx -> {
            ctx.getSource().sendFeedback(Component.literal(
                P + "§7Commands:\n" +
                "  §f/skintotem info\n" +
                "  §f/skintotem refresh\n" +
                "  §f/skintotem tl <nickname>\n" +
                "  §f/skintotem ely <nickname>\n" +
                "  §f/skintotem url <url>\n" +
                "  §f/skintotem credits"
            ));
            return 1;
        };
    }
}
