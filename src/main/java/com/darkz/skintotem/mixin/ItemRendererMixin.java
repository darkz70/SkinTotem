package com.darkz.skintotem.mixin;

//? if <=1.21.3 {

/*import com.llamalad7.mixinextras.sugar.Local;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.thing.ThingMarks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;

import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.extension.ItemStackExtension;

//? <=1.21.1
/^import net.minecraft.client.render.model.json.ModelPart.RotationationMode;^/

import net.minecraft.client.resources.model.BakedModel;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import com.darkz.skintotem.utils.plugin.TotemDollPlugin;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

	@Shadow
	@Final
	private ItemModels models;

	//? if >=1.21.2 {
	@Inject(at = @At(value = "HEAD"), method = "getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;", cancellable = true)
	private void renderDoll(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
	//?} else {
	/^@Inject(at = @At(value = "HEAD"), method = "getModel", cancellable = true)
	private void renderDoll(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
	^///?}
		if (!SkinTotemModClient.canProcess(stack)) {
			return;
		}
		if (TotemDollPlugin.work(stack)) {
			BakedModel model = this.models/^? <=1.21.1 {^/ /^.getModelManager() ^//^?}^/.getModel(TotemDollPlugin.ID);
			stack.setModdedModel(true);
			cir.setReturnValue(model);
		}
	}


	//? if >=1.21.2 {
	@Inject(at = @At(value = "HEAD"), method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelPart.RotationationMode;ZLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;IILnet/minecraft/client/render/model/BakedModel;ZF)V", cancellable = true)
	private void renderDoll(ItemStack stack, ModelPart.RotationationMode renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, boolean useInventoryModel, float z, CallbackInfo ci) {
	//?} else {
	/^@Inject(at = @At(value = "HEAD"), method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelPart.RotationationMode;ZLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;IILnet/minecraft/client/render/model/BakedModel;)V", cancellable = true)
	private void renderDoll(ItemStack stack, ModelPart.RotationationMode renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
	^///?}
		DollRenderContext context = DollRenderContext.of(renderMode);
		if (TotemDollRenderer.sentRenderRequest(matrices, stack, context, light, overlay, 0, vertexConsumers)) {
			ci.cancel();
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "renderItem*")
	private void disableModdedModel(CallbackInfo ci, @Local(argsOnly = true) ItemStack stack) {
		if (stack.hasModdedModel()) {
			stack.setModdedModel(false);
		}
	}

}

*///?}
