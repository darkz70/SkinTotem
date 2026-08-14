package com.darkz.skintotem.yacl.custom.controller.totem;

import dev.isxander.yacl3.api.Option;


import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.minecraft.resources.ResourceLocation;

public interface SkinTotemModelControllerBuilder extends ControllerBuilder<ResourceLocation> {

	static SkinTotemModelControllerBuilder create(Option<ResourceLocation> option) {
		return new SkinTotemModelControllerBuilderImpl(option);
	}
}
