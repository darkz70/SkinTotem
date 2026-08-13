//~ client_fabric_commands

package com.darkz.skintotem.client.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import com.darkz.skintotem.client.command.refresh.RefreshCommand;
import static com.darkz.skintotem.utils.CommandUtils.literal;

public class SkinTotemCommandManager {

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(literal("skin-totem")
				.then(RefreshCommand.getInstance()));
	}
}
