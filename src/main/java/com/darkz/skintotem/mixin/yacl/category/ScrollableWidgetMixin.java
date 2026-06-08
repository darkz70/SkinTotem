package com.darkz.skintotem.mixin.yacl.category;

//? >=1.21.4 {

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.yacl.YACLConfigurationScreen;
import com.darkz.skintotem.yacl.custom.TransparencySprites;

@Mixin(ScrollableWidget.class)
public class ScrollableWidgetMixin {

	//? if >=1.21.6 {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0), method = "drawScrollbar")
	private void renderTransparencyScrollerBackground(GuiGraphics instance, com.mojang.blaze3d.pipeline.RenderPipeline renderPipeline, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(instance, renderPipeline, identifier, x, y, width, height);
			return;
		}
		original.call(instance, renderPipeline, TransparencySprites.SCROLLER_BACKGROUND_SPRITE, x, y, width, height);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0), method = "drawScrollbar")
	private void renderTransparencyScroller(GuiGraphics instance, com.mojang.blaze3d.pipeline.RenderPipeline renderPipeline, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(instance, renderPipeline, identifier, x, y, width, height);
			return;
		}
		original.call(instance, renderPipeline, TransparencySprites.SCROLLER_SPRITE, x, y, width, height);
	}
	//?} else {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0), method = "drawScrollbar")
	private void renderTransparencyScrollerBackground(GuiGraphics context, java.util.function.Function<?, ?> function, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, function, identifier, x, y, width, height);
			return;
		}
		original.call(context, function, TransparencySprites.SCROLLER_BACKGROUND_SPRITE, x, y, width, height);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1), method = "drawScrollbar")
	private void renderTransparencyScroller(GuiGraphics context, java.util.function.Function<?, ?> function, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(Minecraft.getInstance().currentScreen)) {
			original.call(context, function, identifier, x, y, width, height);
			return;
		}
		original.call(context, function, TransparencySprites.SCROLLER_SPRITE, x, y, width, height);
	}
	*///?}

}

//?}
