package com.darkz.skintotem.config.totem;

import lombok.Getter;


import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import com.mojang.serialization.Codec;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.config.other.EnumWithText;

@Getter
public enum SkinTotemSkinType implements StringIdentifiable, EnumWithText {

	STEVE(false),
	PLAYER(true),
	HOLDING_PLAYER(false),
	URL_SKIN(true),
	FILE_SKIN(true),
	TLAUNCHER(true),
	ELY_BY(true);

	public static final Codec<SkinTotemSkinType> CODEC = StringIdentifiable.createCodec(SkinTotemSkinType::values);

	private final boolean needData;

	SkinTotemSkinType(boolean needData) {
		this.needData = needData;
	}

	public Text getText() {
		return SkinTotem.text("modmenu.option.standard_doll_skin_type.%s".formatted(this.asString()));
	}

	public Text getSuggestionText() {
		return SkinTotem.text("modmenu.option.standard_doll_skin_type.%s.suggestion".formatted(this.asString()));
	}

	@Override
	public String asString() {
		return this.name().toLowerCase();
	}
}
