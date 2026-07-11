package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.thing.ThingMarks;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;

import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.extension.ItemStackExtension;

import net.minecraft.world.item.ItemDisplayContext;

import net.minecraft.client.resources.model.BakedModel;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import com.darkz.skintotem.utils.plugin.TotemDollPlugin;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

	@Shadow
	@Final
	private ItemModelShaper itemModelShaper;

	@Inject(at = @At(value = "HEAD"), method = "getModel", cancellable = true)
	private void renderDoll(ItemStack stack, Level world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
		if (!SkinTotemClient.canProcess(stack)) {
			return;
		}
		if (TotemDollPlugin.work(stack)) {
			BakedModel model = this.itemModelShaper.getModelManager() .getModel(TotemDollPlugin.ID);
			stack.setModdedModel(true);
			cir.setReturnValue(model);
		}
	}


	@Inject(at = @At(value = "HEAD"), method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", cancellable = true)
	private void renderDoll(ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		DollRenderContext context = DollRenderContext.of(renderMode);
		if (TotemDollRenderer.sentRenderRequest(matrices, stack, context, light, overlay, 0, vertexConsumers)) {
			ci.cancel();
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "renderStatic*")
	private void disableModdedModel(CallbackInfo ci, @Local(argsOnly = true) ItemStack stack) {
		if (stack.hasModdedModel()) {
			stack.setModdedModel(false);
		}
	}

}

