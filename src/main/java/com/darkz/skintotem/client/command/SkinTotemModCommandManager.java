package com.darkz.skintotem.client.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import com.darkz.skintotem.client.command.refresh.RefreshCommand;
import com.darkz.skintotem.api.MojangAPI;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.network.chat.Component;
//? if >=1.21 {
import net.minecraft.core.component.DataComponents;
//?}
import net.minecraft.world.item.Items;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;

public class SkinTotemModCommandManager {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            // /skintotem or /my-totem-doll
            dispatcher.register(literal("skintotem")
                    .then(RefreshCommand.getInstance()));
            
            dispatcher.register(literal("my-totem-doll")
                    .then(RefreshCommand.getInstance()));

            // /totem <tl|url|ely|mojang> [input]
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
                .then(literal("mojang")
                    .executes(ctx -> {
                        MojangAPI.useFallbackAPI = true;
                        ctx.getSource().sendFeedback(Component.literal("§6[SkinTotem] §aMojang refresh enabled (using fallback API)"));
                        return 1;
                    }))
                .then(literal("model")
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
                            ctx.getSource().sendFeedback(Component.literal("§6[SkinTotem] §aDefault model set to: §f" + modelId));
                            return 1;
                        })))
                .then(argument("nickname", StringArgumentType.word())
                    .executes(ctx -> setTotemName(ctx, StringArgumentType.getString(ctx, "nickname"))))
            );
        });
    }

    private static int setTotemName(com.mojang.brigadier.context.CommandContext<net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource> ctx, String name) {
        var mc = net.minecraft.client.Minecraft.getInstance();
        var player = mc.player;
        if (player == null) return 0;
        
        var stack = player.getMainHandStack();
        if (stack.isEmpty() || !stack.isOf(Items.TOTEM_OF_UNDYING)) {
            ctx.getSource().sendError(Component.literal("§cHold a Totem of Undying in your main hand!"));
            return 0;
        }
        
        //? if >=1.21 {
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        //?} else {
        /*stack.setCustomName(Component.literal(name));
        *///?}
        ctx.getSource().sendFeedback(Component.literal("§6[SkinTotem] §aSkin totem set to: §f" + name));
        return 1;
    }
}
