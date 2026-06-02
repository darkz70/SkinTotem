package com.darkz.skintotem.utils;

import net.minecraft.util.Identifier;

public record ButtonTextures(Identifier enabled, Identifier disabled, Identifier enabledHovered, Identifier disabledHovered) {

	public Identifier get(boolean enabled, boolean hovered) {
		return enabled ? (hovered ? this.enabledHovered : this.enabled) : (hovered ? this.disabledHovered : this.disabled);
	}

}
