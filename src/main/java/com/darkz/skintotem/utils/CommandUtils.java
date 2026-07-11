//~ client_fabric_commands

package com.darkz.skintotem.utils;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

//? if fabric {

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CommandUtils {

	public static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

	public static void sendMessage(Component text) {
		Minecraft.getInstance().gui.getChat().addMessage(text);
	}
}

//?} elif forge {

/*import net.minecraft.commands.CommandSourceStack;

public class CommandUtils {

	public static LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

	public static void sendMessage(Component text) {
		Minecraft.getInstance().gui.getChat().addMessage(text);
	}
}

*///?}
