package com.darkz.skintotem.mixin;

//? if >=1.21.6 {

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.darkz.skintotem.doll.renderer.*;
import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.client.renderer.item.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.21.9 {

 //?}

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

	//? if >=1.21.9 {

	@Shadow private @Nullable ItemStack floatingItem;

	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/item/ItemStackRenderState;render(Lnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V"
			),
			method = "renderFloatingItem"
	)
	private void renderFloatingDoll(ItemStackRenderState instance, PoseStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, int uv, int i, Operation<Void> original) {
		if (!TotemDollRenderer.sentRenderRequest(matrices, this.floatingItem, DollRenderContext.D_FLOATING, light, uv, 0, null)) {
			original.call(instance, matrices, orderedRenderCommandQueue, light, uv, i);
		}
	}

	//?} else {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;IILnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;Lnet/minecraft/world/World;I)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(ItemRenderer instance, ItemStack stack, ItemDisplayContext displayContext, int light, int overlay, PoseStack matrices, MultiBufferSource vertexConsumers, World world, int seed, Operation<Void> original) {
		if (!TotemDollRenderer.sentRenderRequest(matrices, stack, DollRenderContext.D_FLOATING, light, overlay, 0, vertexConsumers)) {
			original.call(instance, stack, displayContext, light, overlay, matrices, vertexConsumers, world, seed);
		}
	}
	*///?}

}
//?}
