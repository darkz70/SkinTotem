package com.darkz.skintotem.gui.tooltip.combined;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.tooltip.TooltipData;

import java.util.List;

public class CombinedTooltipComponent implements ClientTooltipComponent {

    private final List<ClientTooltipComponent> components;

    public CombinedTooltipComponent(List<TooltipData> dataList) {
        this.components = dataList.stream().map(ClientTooltipComponent::create).toList();
    }

    @Override
    public int getHeight(/*? >=1.21.2 {*/Font textRenderer/*?}*/) {
        int height = 0;
        for (ClientTooltipComponent component : this.components) {
            height += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
        }
        return height;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int width = 0;
        for (ClientTooltipComponent component : this.components) {
            int componentWidth = component.getWidth(textRenderer);
            if (componentWidth > width) width = componentWidth;
        }
        return width;
    }

	@Override
	public void renderText(Font textRenderer, int x, int y, GuiGraphics context) {
		int componentY = 0;
		for (ClientTooltipComponent component : this.components) {
			component.renderText(textRenderer, x, y + componentY, context);
			componentY += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
		}
	}

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        int componentY = 0;
        for (ClientTooltipComponent component : this.components) {
            component.renderImage(textRenderer, x, y + componentY, context);
            componentY += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
        }
    }
}
