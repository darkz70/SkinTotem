package com.darkz.skintotem.mixin;

//? if <=1.21.3 {

/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import com.darkz.skintotem.extension.ItemStackExtension;

//? <=1.21.1
/^import net.minecraft.client.render.model.json.ModelPart.RotationationMode;^/

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	//? <=1.21.1 {
	/^@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelPart.RotationationMode;ZLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;Lnet/minecraft/world/World;III)V"), method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelPart.RotationationMode;ZLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;I)V")
	private void render(ItemRenderer instance, LivingEntity entity, ItemStack item, ModelPart.RotationationMode renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, World world, int light, int overlay, int seed, Operation<Void> original) {
	^///?} elif <=1.21.3 {
	/^@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelPart.RotationationMode;ZLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;Lnet/minecraft/world/World;III)V"), method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelPart.RotationationMode;ZLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;I)V")
	private void render(ItemRenderer instance, LivingEntity entity, ItemStack item, ModelPart.RotationationMode renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, World world, int light, int overlay, int seed, Operation<Void> original) {
		^///?}
		//? if <=1.21.3 {
		/^if (entity instanceof AbstractClientPlayer playerEntity) {
			item.setPlayerEntity(playerEntity);
		}
		original.call(instance, entity, item, renderMode, leftHanded, matrices, vertexConsumers, world, light, overlay, seed);
		item.setPlayerEntity(null);
	}
	^///?}
}

*///?}