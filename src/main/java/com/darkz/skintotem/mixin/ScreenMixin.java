package com.darkz.skintotem.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import com.darkz.skintotem.utils.tooltip.TooltipRequest;
import com.darkz.skintotem.utils.tooltip.IRequestableTooltipScreen;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandler implements Renderable, IRequestableTooltipScreen {

	@Shadow public Font font;
	@Unique
	private TooltipRequest skinTotem$tooltipRequest;

	@Inject(at = @At("TAIL"), method = "renderWithTooltip")
	private void renderWithTooltip(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (this.skinTotem$tooltipRequest != null) {
			this.skinTotem$tooltipRequest.render(context, mouseX, mouseY, delta);
			this.skinTotem$tooltipRequest = null;
		}
	}

	@Override
	public void skinTotem$requestTooltip(TooltipRequest tooltipRequest) {
		this.skinTotem$tooltipRequest = tooltipRequest;
	}

	@Override
	public TooltipRequest skinTotem$getCurrentRequest() {
		return this.skinTotem$tooltipRequest;
	}
}
