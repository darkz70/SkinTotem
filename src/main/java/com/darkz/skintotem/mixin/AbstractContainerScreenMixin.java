package com.darkz.skintotem.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.darkz.skintotem.gui.widget.tag.*;
import com.darkz.skintotem.utils.mixin.MTDAnvilScreen;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

	@Inject(at = @At("HEAD"), method = "mouseDragged", cancellable = true)
	private void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
		TagButtonWidget tagButtonWidget = this.myTotemDoll$getTagButtonWidget();
		if (tagButtonWidget == null){
			return;
		}
		if (tagButtonWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "mouseReleased", cancellable = true)
	private void mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		TagButtonWidget tagButtonWidget = this.myTotemDoll$getTagButtonWidget();
		if (tagButtonWidget == null){
			return;
		}
		if (tagButtonWidget.mouseReleased(mouseX, mouseY, button)) {
			cir.setReturnValue(true);
		}
	}

	@Unique
	private @Nullable TagButtonWidget myTotemDoll$getTagButtonWidget() {
		if (!(this instanceof MTDAnvilScreen anvilScreen)) {
			return null;
		}
		return anvilScreen.myTotemDoll$getTagButtonWidget();
	}
}
