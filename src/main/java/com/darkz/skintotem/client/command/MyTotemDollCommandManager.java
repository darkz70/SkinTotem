package com.darkz.skintotem.client.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import com.darkz.skintotem.client.command.refresh.RefreshCommand;
import com.darkz.skintotem.api.MojangAPI;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.text.Text;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class SkinTotemModCommandManager {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            // /skintotem or /my-totem-doll
            dispatcher.register(literal("skintotem")
                    .then(RefreshCommand.getInstance()));
            
            dispatcher.register(literal("my-totem-doll")
                    .then(RefreshCommand.getInstance()));

            // /totem <tl|url|ely|refresh> [input]
            dispatcher.register(literal("totem")
                .then(literal("tl")
                    .then(argument("nickname", StringArgumentType.word())
                        .executes(ctx -> setTotemName(ctx, "tl:" + StringArgumentType.getString(ctx, "nickname")))))
                .then(literal("url")
                    .then(argument("link", StringArgumentType.greedyString())
                        .executes(ctx -> setTotemName(ctx, "url:" + StringArgumentType.getString(ctx, "link")))))
                .then(literal("ely")
                    .then(argument("nickname", StringArgumentType.word())
                        .executes(ctx -> setTotemName(ctx, "ely:" + StringArgumentType.getString(ctx, "nickname")))))
                .then(literal("refresh")
                    .executes(ctx -> {
                        MojangAPI.useFallbackAPI = true;
                        ctx.getSource().sendFeedback(Text.literal("§6[SkinTotem] §aMojang refresh enabled (using fallback API)"));
                        return 1;
                    }))
                .then(argument("nickname", StringArgumentType.word())
                    .executes(ctx -> setTotemName(ctx, StringArgumentType.getString(ctx, "nickname"))))
            );
        });
    }

    private static int setTotemName(com.mojang.brigadier.context.CommandContext<net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource> ctx, String name) {
        var mc = net.minecraft.client.MinecraftClient.getInstance();
        var player = mc.player;
        if (player == null) return 0;
        
        var stack = player.getMainHandStack();
        if (stack.isEmpty() || !stack.isOf(Items.TOTEM_OF_UNDYING)) {
            ctx.getSource().sendError(Text.literal("§cHold a Totem of Undying in your main hand!"));
            return 0;
        }
        
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(name));
        ctx.getSource().sendFeedback(Text.literal("§6[SkinTotem] §aSkin totem set to: §f" + name));
        return 1;
    }
}
