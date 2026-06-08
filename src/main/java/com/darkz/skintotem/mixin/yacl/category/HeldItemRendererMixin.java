package com.darkz.skintotem.mixin.yacl.category;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.isxander.yacl3.gui.YACLScreen;
import java.util.function.*;
import com.darkz.skintotem.client.SkinTotemModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ItemInHandRenderer;

import net.minecraft.client.renderer.item.ItemModelResolver;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.*;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.*;
import com.darkz.skintotem.yacl.YACLConfigurationScreen;
import com.darkz.skintotem.yacl.custom.category.rendering.RenderingCategoryTab;

import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
//?}

//? if >=1.21.9 {

 //?}

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	//? if >=1.21.9 {
	@Inject(
			at = @At("HEAD"),
			method = "renderItem(FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"
	)
	private void createBoolean(CallbackInfo ci, @Share("mtd_bl") LocalBooleanRef ref) {
		createBoolean(ref);
	}

	@WrapOperation(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/item/ItemInHandRenderer$HandRenderType;renderMainHand:Z"
			),
			method = "renderItem(FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"
	)
	private boolean swapRenderValue1(HandRenderType instance, Operation<Boolean> original, @Share("mtd_bl") LocalBooleanRef ref) {
		if (ref.get()) {
			return true;
		}
		return original.call(instance);
	}

	@WrapOperation(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/item/ItemInHandRenderer$HandRenderType;renderOffHand:Z"
			),
			method = "renderItem(FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"
	)
	private boolean swapRenderValue2(HandRenderType instance, Operation<Boolean> original, @Share("mtd_bl") LocalBooleanRef ref) {
		if (ref.get()) {
			return true;
		}
		return original.call(instance);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemInHandRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayer;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V"), method = "renderItem(FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V")
	private void swapRenderingStack(ItemInHandRenderer instance, AbstractClientPlayer playerEntity, float a, float b, Hand hand, float c, ItemStack stack, float d, PoseStack matrixStack, OrderedRenderCommandQueue queue, int i, Operation<Void> original, @Share("mtd_bl") LocalBooleanRef ref) {
		Consumer<ItemStack> consumer = (itemStack) -> original.call(instance, playerEntity, a, b, hand, c, itemStack, d, matrixStack, queue, i);
		if (ref.get()) {
			renderDoll(stack, consumer);
		} else {
			consumer.accept(stack);
		}
	}
	//?} else {
	/*@Inject(
			at = @At("HEAD"),
			method = "renderItem(FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"
	)
	private void createBoolean(CallbackInfo ci, @Share("mtd_bl") LocalBooleanRef ref) {
		createBoolean(ref);
	}

	@WrapOperation(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/item/ItemInHandRenderer$HandRenderType;renderMainHand:Z"
			),
			method = "renderItem(FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"
	)
	private boolean swapRenderValue1(HandRenderType instance, Operation<Boolean> original, @Share("mtd_bl") LocalBooleanRef ref) {
		if (ref.get()) {
			return true;
		}
		return original.call(instance);
	}

	@WrapOperation(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/item/ItemInHandRenderer$HandRenderType;renderOffHand:Z"
			),
			method = "renderItem(FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"
	)
	private boolean swapRenderValue2(HandRenderType instance, Operation<Boolean> original, @Share("mtd_bl") LocalBooleanRef ref) {
		if (ref.get()) {
			return true;
		}
		return original.call(instance);
	}

	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/item/ItemInHandRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayer;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource;I)V"
			),
			method = "renderItem(FLnet/minecraft/client/util/math/PoseStack;Lnet/minecraft/client/render/MultiBufferSource$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"
	)
	private void swapRenderingStack(ItemInHandRenderer instance, AbstractClientPlayer player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, Operation<Void> original, @Share("mtd_bl") LocalBooleanRef ref) {
		Consumer<ItemStack> consumer = (itemStack) -> original.call(instance, player, tickProgress, pitch, hand, swingProgress, itemStack, equipProgress, matrices, vertexConsumers, light);
		if (ref.get()) {
			renderDoll(stack, consumer);
		} else {
			consumer.accept(stack);
		}
	}
	*///?}

	@Unique
	private static void createBoolean(LocalBooleanRef ref) {
		Minecraft client = Minecraft.getInstance();
		Screen currentScreen = client.currentScreen;

		ref.set(false);
		if (YACLConfigurationScreen.notOpen(currentScreen)) {
			return;
		}
		if (!(currentScreen instanceof YACLScreen yaclScreen)) {
			return;
		}
		if (!(yaclScreen.tabManager.getCurrentTab() instanceof RenderingCategoryTab)) {
			return;
		}
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		ref.set(true);
	}

	@Unique
	private static void renderDoll(ItemStack original, Consumer<ItemStack> draw) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player == null) {
			draw.accept(original);
			return;
		}
		if (original.isEmpty() || !SkinTotemModClient.canProcess(original)) {
			ItemStack totem = Items.TOTEM_OF_UNDYING.getDefaultStack();

			//? if >=1.20.5 {
			totem.set(DataComponents.CUSTOM_NAME, player.getName());
			//?} else {
			/*totem.setCustomName(player.getName());
			 *///?}

			draw.accept(totem);
			return;
		}
		draw.accept(original);
	}
}
