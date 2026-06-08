package com.darkz.skintotem.mixin;

//? >=1.21.4 {
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.properties.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataDataComponentType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.utils.mixin.ItemStackRenderStateWithStack;
import com.darkz.skintotem.utils.plugin.TotemDollPlugin;

import java.util.function.Supplier;

//? if >=1.21.9 {

//?}

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {

	//? if >=1.21.9 {
	@Inject(at = @At("HEAD"), method = "update")
	private void captureEntityForDoll(ItemStackRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, World world, HeldItemContext context, int seed, CallbackInfo ci) {
		this.captureEntity(stack, context == null ? null : context.getEntity(), renderState);
	}
	//?} elif >=1.21.5 {
	/*@Inject(at = @At("HEAD"), method = "update")
	private void captureEntityForDoll(ItemStackRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, World world, LivingEntity entity, int seed, CallbackInfo ci) {
		this.captureEntity(stack, entity, renderState);
	}
	*///?}

	//? if >=1.21.5 {
	@WrapOperation(
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/DataComponentType;)Ljava/lang/Object;"
			),
			method = "update"
	)
	private Object swapItemModel(ItemStack stack, DataComponentType<?> componentType, Operation<?> original) {
		return this.changeModel(stack, () -> original.call(stack, componentType));
	}
	//?} else {
	/*@Inject(
			at = @At("HEAD"),
			method = "update(Lnet/minecraft/client/render/item/ItemStackRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelPart.RotationationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V"
	)
	private void captureEntityForDoll(ItemStackRenderState renderState, ItemStack stack, ModelPart.RotationationMode transformationMode, World world, LivingEntity entity, int seed, CallbackInfo ci) {
		this.captureEntity(stack, entity, renderState);
	}

	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/DataComponentType;)Ljava/lang/Object;"
			),
			method = "update(Lnet/minecraft/client/render/item/ItemStackRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelPart.RotationationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V"
	)
	private Object swapItemModel(ItemStack stack, DataComponentType<?> componentType, Operation<?> original) {
		return this.changeModel(stack, () -> original.call(stack, componentType));
	}
	*///?}

	@Unique
	private Object changeModel(ItemStack stack, Supplier<Object> supplier) {
		if (!SkinTotemModClient.canProcess(stack)) {
			return supplier.get();
		}

		if (TotemDollPlugin.work(stack)) {
			stack.setModdedModel(true);
			return TotemDollPlugin.ID;
		}

		stack.setModdedModel(false);
		return supplier.get();
	}

	@Unique
	private void captureEntity(ItemStack stack, @Nullable LivingEntity entity, ItemStackRenderState renderState) {
		stack.setPlayerEntity(null);
		if (entity instanceof AbstractClientPlayer player) {
			stack.setPlayerEntity(player);
		}
		if (renderState instanceof ItemStackRenderStateWithStack itemRenderStateWithStack) {
			itemRenderStateWithStack.myTotemDoll$setStack(stack);
		}
	}

}

//?}
