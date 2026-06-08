package com.darkz.skintotem.doll.data;

import lombok.Getter;
import net.minecraft.network.chat.Component;

import com.darkz.skintotem.SkinTotemMod;

@Getter
public enum LoadingState {

	ERROR, // Y
	CRITICAL_ERROR, // X
	NOT_FOUND, // X
	DESTROYED, // X
	NOT_DOWNLOADED, // Y
	WAITING_DOWNLOADING, // X
	DOWNLOADING, // X
	REGISTERING, // X
	DOWNLOADED; // X

	public Component getText() {
		return SkinTotemMod.text("modmenu.option.standard_doll_skin_type.result.%s".formatted(this.name().toLowerCase()));
	}
}
