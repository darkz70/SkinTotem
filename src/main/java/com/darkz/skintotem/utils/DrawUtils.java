package com.darkz.skintotem.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.*;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DrawUtils {

	public static void drawTexture(GuiGraphics context, Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		context.drawTexture(
				/*? if >=1.21.6 {*/ net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,
				/*?} elif >=1.21.2 {*/ /*net.minecraft.client.render.RenderLayer::getGuiTextured,
				 *//*?}*/
				sprite,
				x,
				y,
				u,
				v,
				width,
				height,
				textureWidth,
				textureHeight
		);
	}

	public static void drawTooltip(GuiGraphics context, List<TooltipComponent> list, int x, int y) {
		context./*? if >=1.21.6 {*/ drawTooltipImmediately /*?} else {*/ /*drawTooltip *//*?}*/(
				Minecraft.getInstance().textRenderer,
				list,
				x,
				y,
				HoveredTooltipPositioner.INSTANCE
				/*? >=1.21.2 {*/,null/*?}*/
		);
	}

	public static void drawCenteredText(GuiGraphics context, Component text, int x, int y, int width) {
		drawCenteredText(context, text, x, y, width, 0);
	}

	public static void drawCenteredText(GuiGraphics context, Component text, int x, int y, int width, int height) {
		TextRenderer textRenderer = Minecraft.getInstance().textRenderer;
		int textWidth = textRenderer.getWidth(text);

		int centerX = x + (width / 2);
		int start = centerX - (textWidth / 2);
		int end = centerX + (textWidth / 2);

		if (start < x || end > x + width) {
			drawScrollableText(context, x, y, width, height, text);
		} else {
			context.drawText(textRenderer, text, start, y + height / 2 - (textRenderer.fontHeight / 2), -1, true);
		}
	}

	public static void drawText(GuiGraphics context, Component text, int x, int y, int width, int height) {
		TextRenderer textRenderer = Minecraft.getInstance().textRenderer;
		int textWidth = textRenderer.getWidth(text);
		if (x + textWidth > x + width) {
			drawScrollableText(context, x, y, width, height, text);
		} else {
			context.drawText(textRenderer, text, x, y + height / 2 - (textRenderer.fontHeight / 2), -1, true);
		}
	}

	private static void drawScrollableText(GuiGraphics context, int x, int y, int width, int height, Component text) {
		//? if >=1.21.11 {
		context.getTextConsumer().text(text, x, x + width, y, y + height);
		//?} else {
		/*TextRenderer textRenderer = Minecraft.getInstance().textRenderer;
		ClickableWidget.drawScrollableText(context, textRenderer, text, x, y, x + width, y + height, -1);
		*///?}
	}
}
