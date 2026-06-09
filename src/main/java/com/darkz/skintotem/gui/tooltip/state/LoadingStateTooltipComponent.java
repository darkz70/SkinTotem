package com.darkz.skintotem.gui.tooltip.state;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.doll.data.LoadingState;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

public class LoadingStateTooltipComponent implements ClientTooltipComponent {

	private final LoadingState state;

	public LoadingStateTooltipComponent(LoadingStateTooltipData data) {
		this.state = data.state();
	}

	@Override
	public int getHeight(/*? >=1.21.2 {*/Font textRenderer/*?}*/) {
		return 10;
	}

	@Override
	public int getWidth(Font textRenderer) {
		return textRenderer.getWidth(this.getText());
	}

	@Override
	public void renderText(Font textRenderer, int x, int y, GuiGraphics context) {
		context.drawText(textRenderer, this.getText(), x, y, -1, true);
	}

	private Component getText() {
		return SkinTotemMod.text("text.loading.%s".formatted(this.state.name().toLowerCase()));
	}
}
