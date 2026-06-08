package com.darkz.skintotem.config.totem;

import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.util.*;

import com.mojang.serialization.Codec;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.config.other.EnumWithText;
import org.jetbrains.annotations.Nullable;

@Getter
public enum TotemDollArmsType implements StringIdentifiable, EnumWithText {

	WIDE,
	SLIM;

	public static final Codec<TotemDollArmsType> CODEC = StringIdentifiable.createCodec(TotemDollArmsType::values);

	public Component getText() {
		return SkinTotemMod.text("modmenu.option.standard_doll_model_arms_type.%s".formatted(this.asString()));
	}

	public static TotemDollArmsType of(boolean slim) {
		return slim ? SLIM : WIDE;
	}

	public static TotemDollArmsType of(@Nullable String s) {
		if (s == null) {
			return WIDE;
		}
		return s.equals("slim") ? SLIM : WIDE;
	}

	@Override
	public String asString() {
		return this.name().toLowerCase();
	}

	public boolean isSlim() {
		return this == SLIM;
	}
}
