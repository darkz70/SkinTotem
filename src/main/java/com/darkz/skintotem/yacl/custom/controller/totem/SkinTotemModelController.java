package com.darkz.skintotem.yacl.custom.controller.totem;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import com.darkz.skintotem.SkinTotem;

public record SkinTotemModelController(Option<Identifier> option) implements Controller<Identifier> {

	@Override
	public Text formatValue() {
		Identifier identifier = this.option.pendingValue();
		return SkinTotem.text("text.nice_id.quoted", identifier.getNamespace(), identifier.getPath());
	}

	@Override
	public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
		return new SkinTotemModelControllerElement(this, screen, widgetDimension);
	}
}
