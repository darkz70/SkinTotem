package com.darkz.skintotem.yacl.custom.controller.totem;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import com.darkz.skintotem.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.network.chat.*;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.yacl.custom.screen.TotemDollModelSelectionScreen;

public class TotemDollModelControllerElement extends ControllerWidget<TotemDollModelController> {

	private final TotemDollModelController controller;
	private final Component selectText;

	public TotemDollModelControllerElement(TotemDollModelController controller, YACLScreen screen, Dimension<Integer> dim) {
		super(controller, screen, dim);
		this.controller = controller;
		this.selectText = SkinTotemMod.text("text.select_text");
	}

	@Override
	protected int getHoveredControlWidth() {
		return this.getUnhoveredControlWidth();
	}

	@Override
	protected Component getValueText() {
		if (this.hovered && this.isAvailable()) {
			return this.selectText;
		}
		return super.getValueText();
	}

	@Override
	protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		TextRenderer textRenderer = Minecraft.getInstance().textRenderer;
		Component valueText = this.getValueText();

		int width = textRenderer.getWidth(valueText);
		if (this.getDimension().x() + this.getXPadding() + width > this.getDimension().xLimit() - this.getXPadding()) {
			DrawUtils.drawText(graphics, valueText, this.getDimension().x() + this.getXPadding(), this.getDimension().y(), this.getDimension().width() - this.getXPadding(), this.getDimension().height());
			return;
		}
		super.drawValueText(graphics, mouseX, mouseY, delta);
	}

	//? if >=1.21.9 {
	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		if (this.isAvailable() && this.isMouseOver(click.x(), click.y()) && this.getDimension().isPointInside((int) click.x(), (int) click.y())) {
			this.playDownSound();
			Minecraft.getInstance().setScreen(new TotemDollModelSelectionScreen(this.screen, this.controller.option()));
			return true;
		}
		return false;
	}
	//?} else {
	/*@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.isAvailable() && this.isMouseOver(mouseX, mouseY) && this.getDimension().isPointInside((int) mouseX, (int) mouseY)) {
			this.playDownSound();
			Minecraft.getInstance().setScreen(new TotemDollModelSelectionScreen(this.screen, this.controller.option()));
			return true;
		}
		return false;
	}
	*///?}
}
