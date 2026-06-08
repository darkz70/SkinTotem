package com.darkz.skintotem.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import com.darkz.skintotem.utils.tooltip.TooltipRequest;
import com.darkz.skintotem.utils.tooltip.IRequestableTooltipScreen;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable, IRequestableTooltipScreen {

	@Shadow public Font textRenderer;
	@Unique
	private TooltipRequest tooltipRequest;

	@Inject(at = @At("TAIL"), method = "renderWithTooltip")
	private void renderWithTooltip(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (this.tooltipRequest != null) {
			//? if >=1.21.6 {
			context.createNewRootLayer();
			//?}
			this.tooltipRequest.render(context, mouseX, mouseY, delta);
			this.tooltipRequest = null;
		}
	}

	@Override
	public void myTotemDoll$requestTooltip(TooltipRequest tooltipRequest) {
		this.tooltipRequest = tooltipRequest;
	}

	@Override
	public TooltipRequest myTotemDoll$getCurrentRequest() {
		return this.tooltipRequest;
	}
}
