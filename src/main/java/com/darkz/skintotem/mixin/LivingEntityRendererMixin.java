package com.darkz.skintotem.mixin;

//? if >=1.21.2 && <=1.21.3 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.item.*;;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.extension.ItemStackExtension;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ModelTransformationMode;)Lnet/minecraft/client/render/model/BakedModel;", ordinal = 0), method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V")
	private BakedModel fixHeadStack(ItemRenderer instance, ItemStack stack, LivingEntity entity, ModelTransformationMode transformationMode, Operation<BakedModel> original, @Local(argsOnly = true) LivingEntityRenderState renderState) {
		BakedModel call = original.call(instance, stack, entity, transformationMode);
		renderState.equippedHeadStack.setModdedModel(stack.hasModdedModel());
		return call;
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ModelTransformationMode;)Lnet/minecraft/client/render/model/BakedModel;", ordinal = 1), method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V")
	private BakedModel fixRightHandStack(ItemRenderer instance, ItemStack stack, LivingEntity entity, ModelTransformationMode transformationMode, Operation<BakedModel> original, @Local(argsOnly = true) LivingEntityRenderState renderState) {
		BakedModel call = original.call(instance, stack, entity, transformationMode);
		renderState.rightHandStack.setModdedModel(stack.hasModdedModel());
		return call;
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ModelTransformationMode;)Lnet/minecraft/client/render/model/BakedModel;", ordinal = 2), method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V")
	private BakedModel fixLeftHandStack(ItemRenderer instance, ItemStack stack, LivingEntity entity, ModelTransformationMode transformationMode, Operation<BakedModel> original, @Local(argsOnly = true) LivingEntityRenderState renderState) {
		BakedModel call = original.call(instance, stack, entity, transformationMode);
		renderState.leftHandStack.setModdedModel(stack.hasModdedModel());
		return call;
	}

}
*///?}
