//~ client_fabric_commands

package com.darkz.skintotem.client.command.refresh;

import java.util.Map;
import java.util.concurrent.*;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.utils.CommandUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import com.darkz.skintotem.api.MojangAPI;
import com.darkz.skintotem.client.command.builder.CommandTextBuilder;
import com.darkz.skintotem.doll.manager.TotemDollManager;
import org.jetbrains.annotations.Nullable;

import static com.darkz.skintotem.utils.CommandUtils.argument;
import static com.darkz.skintotem.utils.CommandUtils.literal;

//? if fabric {

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class RefreshCommand {

	@Nullable
	private static CompletableFuture<Float> RELOADING_ALL_FUTURE = null;
	private static final Map<String, CompletableFuture<Float>> RELOADING_FUTURES = new ConcurrentHashMap<>();

	public static LiteralArgumentBuilder<FabricClientCommandSource> getInstance() {
		return literal("refresh")
				.then(literal("all")
						.executes(RefreshCommand::reloadAll))
				.then(literal("player")
						.then(argument("nickname", StringArgumentType.word())
								.suggests((context, builder) ->
										SharedSuggestionProvider.suggest(TotemDollManager.getAllLoadedKeys(), builder))
								.executes(RefreshCommand::reloadForPlayer)
						));
	}

	private static int reloadAll(CommandContext<FabricClientCommandSource> context) {
		if (RELOADING_ALL_FUTURE != null) {
			return 0;
		}

		Component startFeedback = CommandTextBuilder.startBuilder("command.refresh.all.start").build();
		CommandUtils.sendMessage(startFeedback);

		RELOADING_ALL_FUTURE = TotemDollManager.reloadData((seconds) -> {
			Component endFeedback = CommandTextBuilder.startBuilder("command.refresh.all.end", seconds).build();
			Minecraft.getInstance().execute(() -> CommandUtils.sendMessage(endFeedback));
		}).whenComplete((r, e) -> {
			RELOADING_ALL_FUTURE = null;
			if (e != null) {
				SkinTotemClient.LOGGER.error("Failed to refresh all doll data: ", e);
			}
		});

		MojangAPI.useFallbackAPI = false;

		return Command.SINGLE_SUCCESS;
	}

	private static int reloadForPlayer(CommandContext<FabricClientCommandSource> context) {
		String nickname = StringArgumentType.getString(context, "nickname");

		CompletableFuture<Float> future = RELOADING_FUTURES.get(nickname);
		if (future != null) {
			return 0;
		}

		Component startFeedback = CommandTextBuilder.startBuilder("command.refresh.player.start", nickname).build();
		CommandUtils.sendMessage(startFeedback);

		CompletableFuture<Float> f = TotemDollManager.reloadData(nickname, (seconds) -> {
			Component endFeedback = CommandTextBuilder.startBuilder("command.refresh.player.end", nickname, seconds).build();
			Minecraft.getInstance().execute(() -> CommandUtils.sendMessage(endFeedback));
		});

		if (f != null) {
			CompletableFuture<Float> fc = f.whenComplete((r, e) -> {
				RELOADING_FUTURES.remove(nickname);
				if (e != null) {
					SkinTotemClient.LOGGER.error("Failed to refresh doll data for \"{}\": ", nickname, e);
				}
			});
			RELOADING_FUTURES.put(nickname, fc);
		}

		return Command.SINGLE_SUCCESS;
	}
}

//?} elif forge {

/*import net.minecraft.commands.CommandSourceStack;

public class RefreshCommand {

	@Nullable
	private static CompletableFuture<Float> RELOADING_ALL_FUTURE = null;
	private static final Map<String, CompletableFuture<Float>> RELOADING_FUTURES = new ConcurrentHashMap<>();

	public static LiteralArgumentBuilder<CommandSourceStack> getInstance() {
		return literal("refresh")
				.then(literal("all")
						.executes(RefreshCommand::reloadAll))
				.then(literal("player")
						.then(argument("nickname", StringArgumentType.word())
								.suggests((context, builder) ->
										SharedSuggestionProvider.suggest(TotemDollManager.getAllLoadedKeys(), builder))
								.executes(RefreshCommand::reloadForPlayer)
						));
	}

	private static int reloadAll(CommandContext<CommandSourceStack> context) {
		if (RELOADING_ALL_FUTURE != null) {
			return 0;
		}

		Component startFeedback = CommandTextBuilder.startBuilder("command.refresh.all.start").build();
		CommandUtils.sendMessage(startFeedback);

		RELOADING_ALL_FUTURE = TotemDollManager.reloadData((seconds) -> {
			Component endFeedback = CommandTextBuilder.startBuilder("command.refresh.all.end", seconds).build();
			Minecraft.getInstance().execute(() -> CommandUtils.sendMessage(endFeedback));
		}).whenComplete((r, e) -> {
			RELOADING_ALL_FUTURE = null;
			if (e != null) {
				SkinTotemClient.LOGGER.error("Failed to refresh all doll data: ", e);
			}
		});

		MojangAPI.useFallbackAPI = false;

		return Command.SINGLE_SUCCESS;
	}

	private static int reloadForPlayer(CommandContext<CommandSourceStack> context) {
		String nickname = StringArgumentType.getString(context, "nickname");

		CompletableFuture<Float> future = RELOADING_FUTURES.get(nickname);
		if (future != null) {
			return 0;
		}

		Component startFeedback = CommandTextBuilder.startBuilder("command.refresh.player.start", nickname).build();
		CommandUtils.sendMessage(startFeedback);

		CompletableFuture<Float> f = TotemDollManager.reloadData(nickname, (seconds) -> {
			Component endFeedback = CommandTextBuilder.startBuilder("command.refresh.player.end", nickname, seconds).build();
			Minecraft.getInstance().execute(() -> CommandUtils.sendMessage(endFeedback));
		});

		if (f != null) {
			CompletableFuture<Float> fc = f.whenComplete((r, e) -> {
				RELOADING_FUTURES.remove(nickname);
				if (e != null) {
					SkinTotemClient.LOGGER.error("Failed to refresh doll data for \"{}\": ", nickname, e);
				}
			});
			RELOADING_FUTURES.put(nickname, fc);
		}

		return Command.SINGLE_SUCCESS;
	}
}

*///?}
