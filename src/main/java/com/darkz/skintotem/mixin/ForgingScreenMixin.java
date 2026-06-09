package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import java.util.function.*;
import com.darkz.skintotem.config.SkinTotemModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.utils.mixin.MTDAnvilScreen;

//? if >=1.21.6 {

import com.mojang.blaze3d.pipeline.RenderPipeline;

//?}

@Mixin(AnvilScreen.class)
public class ForgingScreenMixin {

	//? if >=1.21.6 {
	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V"
			),
			method = "drawBackground"
	)
	private void drawBackground(GuiGraphics instance, com.mojang.blaze3d.pipeline.RenderPipeline renderPipeline, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, Operation<Void> original) {
		Consumer<Integer> draw = (w) -> original.call(instance, renderPipeline, identifier, x, y, u, v, w, height, textureWidth, textureHeight);
		this.drawBackground(width, draw);
	}
	//?} elif >=1.21.2 {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIFFIIII)V"), method = "drawBackground")
	private void drawBackground(GuiGraphics instance, Function<?, ?> function, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, Operation<Void> original) {
		Consumer<Integer> draw = (w) -> original.call(instance, function, identifier, x, y, u, v, w, height, textureWidth, textureHeight);
		this.drawBackground(width, draw);
	}
	*///?} elif <=1.21.1 {
	/*@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"
			),
			method = "drawBackground"
	)
	private void drawBackground(GuiGraphics instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
		Consumer<Integer> draw = (w) -> original.call(instance, texture, x, y, u, v, w, height);
		this.drawBackground(width, draw);
	}
	*///?}

	@Unique
	private void drawBackground(int width, Consumer<Integer> draw) {
		if (this instanceof MTDAnvilScreen && SkinTotemModConfig.getInstance().isModEnabled()) {
			draw.accept(176);
			return;
		}
		draw.accept(width);
	}

}