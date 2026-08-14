package com.darkz.skintotem.yacl.custom.controller.totem;

import dev.isxander.yacl3.api.*;
import net.minecraft.resources.ResourceLocation;

public class SkinTotemModelControllerBuilderImpl implements SkinTotemModelControllerBuilder {

	private final Option<ResourceLocation> option;

	public SkinTotemModelControllerBuilderImpl(Option<ResourceLocation> option) {
		this.option = option;
	}

	@Override
	public Controller<ResourceLocation> build() {
		return new SkinTotemModelController(this.option);
	}
}
