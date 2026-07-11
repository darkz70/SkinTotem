//~ client_fabric_commands

package com.darkz.skintotem.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.darkz.skintotem.client.command.refresh.RefreshCommand;
import static com.darkz.skintotem.utils.CommandUtils.literal;

//? if fabric {

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class SkinTotemCommandManager {

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(literal("skintotem")
				.then(RefreshCommand.getInstance())
				.then(SkinTotemCommand.getInfoCommand())
				.then(SkinTotemCommand.getCreditsCommand())
				.then(SkinTotemCommand.getTlCommand())
				.then(SkinTotemCommand.getElyCommand())
				.then(SkinTotemCommand.getUrlCommand())
				.then(SkinTotemCommand.getMojangCommand())
				.then(SkinTotemCommand.getModelCommand())
				.executes(SkinTotemCommand.getHelpExecutor())
		);
	}
}

//?} elif forge {

/*import net.minecraft.commands.CommandSourceStack;

public class SkinTotemCommandManager {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(literal("skintotem")
				.then(RefreshCommand.getInstance())
				.then(SkinTotemCommand.getInfoCommand())
				.then(SkinTotemCommand.getCreditsCommand())
				.then(SkinTotemCommand.getTlCommand())
				.then(SkinTotemCommand.getElyCommand())
				.then(SkinTotemCommand.getUrlCommand())
				.then(SkinTotemCommand.getMojangCommand())
				.then(SkinTotemCommand.getModelCommand())
				.executes(SkinTotemCommand.getHelpExecutor())
		);
	}
}

*///?}
