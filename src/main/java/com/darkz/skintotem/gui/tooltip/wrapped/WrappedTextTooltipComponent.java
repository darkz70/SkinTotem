package com.darkz.skintotem.gui.tooltip.wrapped;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.network.chat.*;;

import java.util.List;

public class WrappedTextTooltipComponent implements TooltipComponent {

	private final List<OrderedText> texts;

	public WrappedTextTooltipComponent(Text text) {
		this.texts = Minecraft.getInstance().textRenderer.wrapLines(text, 100000);
	}

	public int getWidth(TextRenderer textRenderer) {
		int max = 0;
		for (OrderedText text : this.texts) {
			int width = textRenderer.getWidth(text);
			if (width > max) {
				max = width;
			}
		}
		return max;
	}

	@Override
	public int getHeight(/*? >=1.21.2 {*/TextRenderer textRenderer/*?}*/) {
		return this.texts.size() * 10;
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, /*? >=1.21.2 {*/int w, int h,/*?}*/ DrawContext context) {
		int offset = 0;
		for (OrderedText text : this.texts) {
			context.drawText(textRenderer, text, x, y + offset, -1, true);
			offset += 10;
		}
	}
}
