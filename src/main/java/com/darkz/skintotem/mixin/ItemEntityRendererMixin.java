package com.darkz.skintotem.mixin;

//? if >=1.21.2 && <=1.21.3 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import com.darkz.skintotem.extension.ItemStackExtension;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"), method = "updateRenderState(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;F)V")
	private BakedModel fixStacks(ItemRenderer instance, ItemStack stack, World world, LivingEntity entity, int seed, Operation<BakedModel> original, @Local(argsOnly = true) ItemEntityRenderState renderState) {
		BakedModel call = original.call(instance, stack, world, entity, seed);
		renderState.stack.setModdedModel(stack.hasModdedModel());
		return call;
	}

}
*///?}
