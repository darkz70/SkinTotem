package com.darkz.skintotem.mixin.yacl.category;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;

//? <=1.21.3 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.injection.At;

import com.mojang.blaze3d.systems.RenderSystem;

import com.darkz.skintotem.yacl.YACLConfigurationScreen;
import com.darkz.skintotem.yacl.custom.TransparencySprites;
import java.util.function.Function;
*///?}

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin /*? >=1.20.3 {*/ extends AbstractWidget /*?}*/ {

	@Unique
	private static final String RENDER_METHOD = /*? >=1.20.3 {*/ "renderWidget" /*?} else {*/ /*"render" *//*?}*/;

	//? <=1.20.2 {
	/*@Shadow
	protected int bottom;
	@Shadow
	protected int top;
	@Shadow
	protected int width;
	@Shadow
	protected int height;
	*///?}

	//? >=1.20.3 {
	public EntryListWidgetMixin(int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
	}
	//?}

	//? if =1.21.2 || =1.21.3 {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0), method = "renderWidget")
	private void renderTransparencyScrollerBackground(GuiGraphics context, Function<?, ?> function, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, function, identifier, x, y, width, height);
			return;
		}
		original.call(context, function, TransparencySprites.SCROLLER_BACKGROUND_SPRITE, x, y, width, height);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1), method = RENDER_METHOD)
	private void renderTransparencyScroller(GuiGraphics context, Function<?, ?> function, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, function, identifier, x, y, width, height);
			return;
		}
		original.call(context, function, TransparencySprites.SCROLLER_SPRITE, x, y, width, height);
	}
	*///?} elif >=1.20.5 && <=1.21.3 {

	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0), method = RENDER_METHOD)
	private void renderTransparencyScrollerBackground(GuiGraphics context, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, texture, x, y, width, height);
			return;
		}
		original.call(context, TransparencySprites.SCROLLER_BACKGROUND_SPRITE, x, y, width, height);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1), method = RENDER_METHOD)
	private void renderTransparencyScroller(GuiGraphics context, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, texture, x, y, width, height);
			return;
		}
		original.call(context, TransparencySprites.SCROLLER_SPRITE, x, y, width, height);
	}

	*///?} elif <=1.21.3 {

	/*@Shadow
	protected abstract int getScrollbarPositionX();

	@ModifyExpressionValue(at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;renderBackground:Z"), method = RENDER_METHOD)
	private boolean disableBackgroundRendering(boolean original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			return original;
		}
		return false;
	}

	*///?}

	//? <=1.20.1 {

	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 0), method = "render")
	private void renderTransparencyScrollerBackground1(GuiGraphics context, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, x1, y1, x2, y2, color);
			return;
		}
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		context.drawNineSlicedTexture(TransparencySprites.SCROLLER_BACKGROUND_SPRITE, this.getScrollbarPositionX(), this.top, 6, this.height, 1, 6, 32, 0, 0);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 1), method = "render")
	private void renderTransparencyScroller2(GuiGraphics context, int x, int y, int width, int height, int color, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, x, y, width, height, color);
			return;
		}

		context.drawNineSlicedTexture(TransparencySprites.SCROLLER_SPRITE, this.getScrollbarPositionX(), y, 6, height - y, 1, 6, 32, 0, 0);
		RenderSystem.disableBlend();
		RenderSystem.disableDepthTest();
	}

	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 2), method = "render")
	private boolean renderTransparencyScrollerBackground3(GuiGraphics instance, int x1, int y1, int x2, int y2, int color) {
		return YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen);
	}

	*///?}

	//? <=1.20.1 {

	/*@ModifyExpressionValue(at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;renderHorizontalShadows:Z"), method = "render")
	private boolean disableShadows(boolean original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			return original;
		}
		return false;
	}

	*///?}
}
