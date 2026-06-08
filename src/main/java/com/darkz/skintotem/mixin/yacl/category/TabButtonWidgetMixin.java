package com.darkz.skintotem.mixin.yacl.category;

//? if <=1.20.4 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import com.darkz.skintotem.yacl.YACLConfigurationScreen;
import com.darkz.skintotem.yacl.custom.TransparencySprites;

@Mixin(TabButtonWidget.class)
public abstract class TabButtonWidgetMixin extends AbstractWidget {

	public TabButtonWidgetMixin(int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
	}

	@Unique
	private static final String RENDER_METHOD = /^? >=1.20.3 {^/ "renderWidget" /^?} else {^/ /^"renderButton" ^//^?}^/;
	@Unique
	private static final String WRAP_TARGET = /^? >=1.20.2 {^/ "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V" /^?} else {^/ /^"Lnet/minecraft/client/gui/GuiGraphics;drawNineSlicedTexture(Lnet/minecraft/util/Identifier;IIIIIIIIIIII)V" ^//^?}^/;


	@Shadow
	public abstract boolean isCurrentTab();

	//? if <=1.20.1 {
	/^@WrapOperation(at = @At(value = "INVOKE", target = WRAP_TARGET), method = "renderButton")
	private void renderTransparencyTab1(GuiGraphics context, Identifier identifier, int x, int y, int w, int h, int a, int b, int c, int d, int e, int k, int l, int u, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, identifier, x, y, w, h, a, b, c, d, e, k, l, u);
			return;
		}

		RenderSystem.enableBlend();
		context.drawNineSlicedTexture(TransparencySprites.TAB_BUTTON_SPRITES.get(this.isCurrentTab(), this.isSelected()), x, y, this.width, this.height, 2, 130, 24, 0, 0);
		RenderSystem.disableBlend();
	}
	^///?} else {
	
	@WrapOperation(at = @At(value = "INVOKE", target = WRAP_TARGET), method = RENDER_METHOD)
	private void renderTransparencyTab2(GuiGraphics context, Identifier textureId, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, textureId, x, y, width, height);
			return;
		}

		RenderSystem.enableBlend();
		context.drawGuiTexture(TransparencySprites.TAB_BUTTON_SPRITES.get(this.isCurrentTab(), this.isSelected()), x, y, width, height);
		RenderSystem.disableBlend();
	}
	//?}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TabButtonWidget;drawCurrentTabLine(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/font/Font;I)V"), method = RENDER_METHOD)
	private void renderTabBackground(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		int left = this.getX() + 2;
		int top = this.getY() + 2;
		//? if >=1.20.3 {
		int right = this.getRight() - 2;
		int bottom = this.getBottom();
		//?} else {
		/^int right = (this.getX() + this.getWidth()) - 2;
		int bottom = (this.getY() + this.getHeight());
		^///?}

		RenderSystem.enableBlend();
		context.drawTexture(TransparencySprites.getMenuListBackgroundTexture(), left, top, 0, 0, right - left, bottom - top);
		RenderSystem.disableBlend();
	}


}
*///?}
