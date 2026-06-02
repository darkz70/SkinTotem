package com.darkz.skintotem;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kteam.skintotem.config.SkinTotemConfig;
import ru.kteam.skintotem.command.SkinTotemCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class SkinTotemMod implements ModInitializer {

    public static final String MOD_ID = "skintotem";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final SkinTotemConfig CONFIG = new SkinTotemConfig();

    @Override
    public void onInitialize() {
        LOGGER.info("[SkinTotem] Initialized");

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            SkinTotemCommand.register(dispatcher);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(
                CommandManager.literal("totem")
                    .then(CommandManager.argument("input", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            var player = ctx.getSource().getPlayer();
                            if (player == null) {
                                ctx.getSource().sendError(Text.literal("Only players can use this command."));
                                return 0;
                            }
                            var stack = player.getMainHandStack();
                            if (stack.isEmpty() || !stack.isOf(Items.TOTEM_OF_UNDYING)) {
                                ctx.getSource().sendError(
                                    Text.literal("§cHold a Totem of Undying in your main hand!"));
                                return 0;
                            }
                            String input = StringArgumentType.getString(ctx, "input").trim();
                            stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(input));
                            ctx.getSource().sendFeedback(
                                () -> Text.literal("§aSkin totem set to: §f" + input), false);
                            return 1;
                        })
                    )
            )
        );
    }
}
