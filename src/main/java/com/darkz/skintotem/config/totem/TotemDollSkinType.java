package com.darkz.skintotem.config.totem;

import lombok.Getter;


import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import com.mojang.serialization.Codec;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.config.other.EnumWithText;

@Getter
public enum TotemDollSkinType implements StringIdentifiable, EnumWithText {

	STEVE(false),
	PLAYER(true),
	HOLDING_PLAYER(false),
	URL_SKIN(true),
	FILE_SKIN(true),
	TLAUNCHER(true),
	ELY_BY(true);

	public static final Codec<TotemDollSkinType> CODEC = StringIdentifiable.createCodec(TotemDollSkinType::values);

	private final boolean needData;

	TotemDollSkinType(boolean needData) {
		this.needData = needData;
	}

	public Text getText() {
		return SkinTotemMod.text("modmenu.option.standard_doll_skin_type.%s".formatted(this.asString()));
	}

	public Text getSuggestionText() {
		return SkinTotemMod.text("modmenu.option.standard_doll_skin_type.%s.suggestion".formatted(this.asString()));
	}

	@Override
	public String asString() {
		return this.name().toLowerCase();
	}
}
