package com.darkz.skintotem.gui.tooltip.wrapped;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.*;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class WrappedTextTooltipComponent implements ClientTooltipComponent {

	private final List<FormattedCharSequence> texts;

	public WrappedTextTooltipComponent(Component text) {
		this.texts = Minecraft.getInstance().textRenderer.split(text, 200);
	}

	public int getWidth(Font textRenderer) {
		int max = 0;
		for (FormattedCharSequence text : this.texts) {
			int width = textRenderer.getWidth(text);
			if (width > max) {
				max = width;
			}
		}
		return max;
	}

	@Override
	public int getHeight(/*? >=1.21.2 {*/Font textRenderer/*?}*/) {
		return this.texts.size() * 10;
	}

	@Override
	public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
		int offset = 0;
		for (FormattedCharSequence text : this.texts) {
			context.drawText(textRenderer, text, x, y + offset, -1, true);
			offset += 10;
		}
	}
}
