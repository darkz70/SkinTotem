package com.darkz.skintotem.mixin;

//? if >=1.21.4 {
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.utils.mixin.ItemStackRenderStateWithStack;

import org.jetbrains.annotations.Nullable;

//? if >=1.21.9 {

 //?}

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemStackRenderState.class)
public class ItemRenderStateMixin implements ItemStackRenderStateWithStack {

	//? if <=1.21.4 {
	/*@Shadow
	ModelPart.RotationationMode modelItemTransformMode;
	@Shadow
	boolean leftHand;
	*///?} else {
	@Shadow ItemDisplayContext displayContext;
	//?}

	@Unique
	@Nullable
	private ItemStack stack;

	@Unique
	private boolean shouldClear = true;

	//? if >=1.21.9 {
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(PoseStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, int outlineColor, CallbackInfo ci) {
		this.renderDoll(matrices, light, overlay, outlineColor, null, ci);
	}
	//?} else {
	/*@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(PoseStack matrices, MultiBufferSource provider, int light, int overlay, CallbackInfo ci) {
		this.renderDoll(matrices, light, overlay, 0, provider, ci);
	}
	*///?}

	@Unique
	private void renderDoll(PoseStack matrices, int light, int overlay, @SuppressWarnings("all") int outlineColor, @Nullable MultiBufferSource provider, CallbackInfo ci) {
		DollRenderContext context = DollRenderContext.of(/*? if <=1.21.4 {*//*this.modelItemTransformMode*//*?} else {*/ this.displayContext /*?}*/);

		if (this.stack != null) {
			if (TotemDollRenderer.sentRenderRequest(matrices, this.stack, context, light, overlay, outlineColor, provider)) {
				ci.cancel();
			}
		}

		if (this.shouldClear) {
			if (this.stack != null && this.stack.hasModdedModel()) {
				this.stack.setModdedModel(false);
			}
			this.stack = null;
		}
	}

	@Override
	public void myTotemDoll$setStack(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public void myTotemDoll$shouldClear(boolean bl) {
		this.shouldClear = bl;
	}
}

//?}