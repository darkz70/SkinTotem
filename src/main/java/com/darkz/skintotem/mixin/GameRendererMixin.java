package com.darkz.skintotem.mixin;

//? if <=1.21.5 {

/*import com.darkz.skintotem.doll.renderer.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

//? if <=1.21.1 {
/^import net.minecraft.client.render.model.json.ModelPart.RotationationMode;
 ^///?}

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	//? if >=1.21.2 && <=1.21.5 {
	/^@Shadow
	@Nullable
	private ItemStack floatingItem;


	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;draw(Ljava/util/function/Consumer;)V"
			),
			method = "renderFloatingItem"
	)
	private void renderFloatingDoll(GuiGraphics drawContext, Consumer<?> drawCallback, Operation<Void> original, @Local PoseStack matrices) {
		drawContext.draw((sus) -> {
			if (!TotemDollRenderer.sentRenderRequest(matrices, this.floatingItem, DollRenderContext.D_FLOATING, 15728880, OverlayTexture.DEFAULT_UV, 0, drawContext.vertexConsumers)) {
				original.call(drawContext, drawCallback);
			}
		});
	}
	^///?} elif >=1.21 {

	@Shadow
	@Nullable
	private ItemStack floatingItem;

	@SuppressWarnings("deprecation")
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;draw(Ljava/lang/Runnable;)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(GuiGraphics drawContext, Runnable drawCallback, Operation<Void> original, @Local PoseStack matrices) {
		drawContext.draw(() -> {
			if (!TotemDollRenderer.sentRenderRequest(matrices, this.floatingItem, DollRenderContext.D_FLOATING, 15728880, OverlayTexture.DEFAULT_UV, 0, drawContext.vertexConsumers)) {
				original.call(drawContext, drawCallback);
			}
		});
	}

	//?} else {

	/^@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelPart.RotationationMode;IILnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;Lnet/minecraft/world/World;I)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(ItemRenderer itemRenderer, ItemStack stack, ModelPart.RotationationMode transformationType, int light, int overlay, PoseStack matrices, MultiBufferSource vertexConsumers, World world, int seed, Operation<Void> original) {
		if (!TotemDollRenderer.sentRenderRequest(matrices, stack, DollRenderContext.D_FLOATING, light, overlay, 0, vertexConsumers)) {
			original.call(itemRenderer, stack, transformationType, light, overlay, matrices, vertexConsumers, world, seed);
		}
	}

	^///?}
}

*///?}
