package com.darkz.skintotem.gui.tooltip.info;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.utils.*;

//? if >=1.21.11 {
import net.minecraft.client.gui.components.MultiLineLabel;
//?}

//? if >=1.21.9 && <=1.21.10 {
/*

*/
//?}

public class InfoTooltipComponent implements TooltipComponent {

	public static final Identifier SEPARATOR = SkinTotemMod.id("textures/gui/info/separator.png");

	private final MutableComponent title;
	private final MultiLineLabel text;

	public InfoTooltipComponent(String key, int color) {
		this.title = SkinTotemMod.text("%s.title".formatted(key));
		this.title.setStyle(this.title.getStyle().withColor(color));
		this.text  = MultiLineLabel.create(Minecraft.getInstance().textRenderer, SkinTotemMod.text("%s.text".formatted(key)), 140);
	}

	@Override
	public int getHeight(/*? >=1.21.2 {*/Font textRenderer/*?}*/) {
		return (this.text./*? if >=1.21.9 {*/ getLineCount /*?} else {*/ /*count *//*?}*/() * 10) + 26 + 2 + 5 + 2 + 5;
	}

	@Override
	public int getWidth(Font textRenderer) {
		return 150;
	}

	@Override
	public void drawItems(Font textRenderer, int x, int y, /*? >=1.21.2 {*/int w, int h,/*?}*/ GuiGraphics context) {
		int width = this.getWidth(textRenderer);
		int titleWidth = textRenderer.getWidth(this.title);
		context.drawText(textRenderer, this.title, x + (((width) / 2) - (titleWidth / 2)), y + 8, -1, false);
		DrawUtils.drawTexture(context, SEPARATOR, x, y + 24, 0, 0, 150, 5, 150, 5);
		//? if >=1.21.11 {
		this.text.draw(Alignment.LEFT, x + 5, y + 26 + 2 + 5 + 2, 10, context.getTextConsumer());
		//?} elif >=1.21.9 {
		/*this.text.draw(context, Alignment.LEFT, x + 5, y + 26 + 2 + 5 + 2, 10, true, -1);
		*///?} else {
		/*this.text.draw(context, x + 5, y + 26 + 2 + 5 + 2, 10, -1);
		*///?}
	}
}
