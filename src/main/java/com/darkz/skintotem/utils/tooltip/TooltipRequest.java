package com.darkz.skintotem.utils.tooltip;

import net.minecraft.client.gui.GuiGraphics;

public interface TooltipRequest {

	void render(GuiGraphics context, int mouseX, int mouseY, float delta);

}
