package com.darkz.skintotem.gui.tooltip.combined;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

import java.util.List;

public class CombinedTooltipComponent implements TooltipComponent {

    private final List<TooltipComponent> components;

    public CombinedTooltipComponent(List<TooltipComponent> components) {
        this.components = components;
    }

    @Override
    public int getHeight(/*? >=1.21.2 {*/Font textRenderer/*?}*/) {
        int height = 0;
        for (TooltipComponent component : this.components) {
            height += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
        }
        return height;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int width = 0;
        for (TooltipComponent component : this.components) {
            int componentWidth = component.getWidth(textRenderer);
            if (componentWidth > width) width = componentWidth;
        }
        return width;
    }

	//? if >=1.21.6 {
	@Override
	public void drawText(GuiGraphics context, Font textRenderer, int x, int y) {
		int componentY = 0;
		for (TooltipComponent component : this.components) {
			component.drawText(context, textRenderer, x, y + componentY);
			componentY += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
		}
	}
	//?} else {
	/*@Override
	public void drawText(Font textRenderer, int x, int y, Matrix4f matrix, Immediate vertexConsumers) {
		int componentY = 0;
		for (TooltipComponent component : this.components) {
			component.drawText(textRenderer, x, y + componentY, matrix, vertexConsumers);
			componentY += component.getHeight(/^? >=1.21.2 {^/textRenderer/^?}^/) + 1;
		}
	}
	*///?}

    @Override
    public void drawItems(Font textRenderer, int x, int y, /*? >=1.21.2 {*/int w, int h,/*?}*/ GuiGraphics context) {
        int componentY = 0;
        for (TooltipComponent component : this.components) {
            component.drawItems(textRenderer, x, y + componentY, /*? >=1.21.2 {*/ w, h,/*?}*/ context);
            componentY += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
        }
    }
}