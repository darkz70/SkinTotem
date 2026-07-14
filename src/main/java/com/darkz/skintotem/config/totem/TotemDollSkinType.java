package com.darkz.skintotem.config.totem;

import lombok.Getter;


import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import com.mojang.serialization.Codec;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.config.other.EnumWithText;
import net.minecraft.util.StringRepresentable.EnumCodec;

@Getter
public enum TotemDollSkinType implements StringRepresentable, EnumWithText {

	STEVE(false),
	PLAYER(true),
	HOLDING_PLAYER(false),
	URL_SKIN(true),
	FILE_SKIN(true),
	ELY_BY(true),
	TLAUNCHER(true);

	public static final EnumCodec<TotemDollSkinType> CODEC = StringRepresentable.fromEnum(TotemDollSkinType::values);

	private final boolean needData;

	TotemDollSkinType(boolean needData) {
		this.needData = needData;
	}

	public Component getText() {
		return SkinTotem.text("modmenu.option.standard_doll_skin_type.%s".formatted(this.getSerializedName()));
	}

	public Component getSuggestionText() {
		return SkinTotem.text("modmenu.option.standard_doll_skin_type.%s.suggestion".formatted(this.getSerializedName()));
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase();
	}
}
