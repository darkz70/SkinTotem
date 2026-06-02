package com.darkz.skintotem.mixin.yacl.widget;

import dev.isxander.yacl3.gui.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(OptionDescriptionWidget.class)
public class OptionDescriptionWidgetMixin {

	@Dynamic
	@Shadow(remap = false)
	private float currentScrollAmount;
	@Dynamic
	@Shadow(remap = false)
	private float targetScrollAmount;

	@Dynamic
	@Shadow(remap = false)
	private int maxScrollAmount;

	@Unique
	private float currentScroll, targetScroll;

	@Dynamic
	@Inject(at = @At("HEAD"), method = "setOptionDescription", remap = false)
	private void saveScroll(DescriptionWithName description, CallbackInfo ci) {
		this.currentScroll = this.currentScrollAmount;
		this.targetScroll  = this.targetScrollAmount;
	}

	@Dynamic
	@Inject(at = @At("TAIL"), method = "setOptionDescription", remap = false)
	private void loadScroll(DescriptionWithName description, CallbackInfo ci) {
		this.currentScrollAmount = this.currentScroll;
		this.targetScrollAmount  = this.targetScroll;
	}

	@Dynamic
	@Inject(at = @At("TAIL"), method = /*? if >=1.21 {*/ "renderWidget" /*?} else {*/ /*"renderButton" *//*?}*/)
	private void fixMaxScroll(CallbackInfo ci) {
		if (this.targetScrollAmount > this.maxScrollAmount) {
			this.targetScrollAmount = this.maxScrollAmount;
		}
	}
}
