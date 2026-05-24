package com.darkz.skintotem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkinTotemMod implements ModInitializer {

    public static final String MOD_ID = "skintotem";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SkinTotem mod initialized");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                CommandManager.literal("totem")
                    .then(CommandManager.argument("input", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            String input = StringArgumentType.getString(ctx, "input");
                            var player = ctx.getSource().getPlayer();
                            if (player == null) {
                                ctx.getSource().sendError(Text.literal("This command must be run by a player."));
                                return 0;
                            }

                            var heldItem = player.getMainHandStack();
                            if (heldItem.isEmpty() || !heldItem.isOf(net.minecraft.item.Items.TOTEM_OF_UNDYING)) {
                                ctx.getSource().sendError(Text.literal("Hold a Totem of Undying in your main hand!"));
                                return 0;
                            }

                            // Store the input (nickname or URL) in the item's custom name
                            heldItem.set(net.minecraft.component.DataComponentTypes.CUSTOM_NAME,
                                Text.literal(input));

                            ctx.getSource().sendFeedback(() ->
                                Text.literal("Totem skin set to: " + input), false);
                            return 1;
                        })
                    )
            );
        });
    }
}
