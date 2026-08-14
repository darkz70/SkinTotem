package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import java.util.function.*;
import com.darkz.skintotem.config.SkinTotemConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.utils.mixin.MTDAnvilScreen;


@Mixin(ItemCombinerScreen.class)
public class ItemCombinerScreenMixin {

	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
			),
			method = "renderBg"
	)
	private void drawBackground(GuiGraphics instance, ResourceLocation texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
		Consumer<Integer> draw = (w) -> original.call(instance, texture, x, y, u, v, w, height);
		this.mySkinTotem$drawBackground(width, draw);
	}

	@Unique
	private void mySkinTotem$drawBackground(int width, Consumer<Integer> draw) {
		if (this instanceof MTDAnvilScreen && SkinTotemConfig.getInstance().isModEnabled()) {
			draw.accept(176);
			return;
		}
		draw.accept(width);
	}

}