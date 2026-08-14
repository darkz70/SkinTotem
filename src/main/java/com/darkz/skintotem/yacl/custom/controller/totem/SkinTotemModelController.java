package com.darkz.skintotem.yacl.custom.controller.totem;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.SkinTotem;

public record SkinTotemModelController(Option<ResourceLocation> option) implements Controller<ResourceLocation> {

	@Override
	public Component formatValue() {
		ResourceLocation identifier = this.option.pendingValue();
		return SkinTotem.text("text.nice_id.quoted", identifier.getNamespace(), identifier.getPath());
	}

	@Override
	public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
		return new SkinTotemModelControllerElement(this, screen, widgetDimension);
	}
}
