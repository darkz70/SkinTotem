package com.darkz.skintotem.client.command.builder;

import net.minecraft.network.chat.*;

import com.darkz.skintotem.SkinTotemMod;

public class CommandTextBuilder {

	private static final MutableComponent MOD_ID_TEXT = SkinTotemMod.text("command.id");

	private final MutableComponent text;

	private CommandTextBuilder(String key, Object... args) {
		this.text = CommandTextBuilder.translatable(key, args);
	}

	private static MutableComponent translatable(String key, Object... args) {
		for (int i = 0; i < args.length; ++i) {
			Object object = args[i];
			if (!isPrimitive(object) && !(object instanceof Component)) {
				args[i] = String.valueOf(object);
			}
		}

		return SkinTotemMod.text(key, args);
	}

	private static boolean isPrimitive(Object object) {
		return object instanceof Number || object instanceof Boolean || object instanceof String;
	}

	public static CommandTextBuilder startBuilder(String key, Object... args) {
		return new CommandTextBuilder(key, args);
	}

	public Component build() {
		return MOD_ID_TEXT.copy().append(" ").append(this.text);
	}
}
