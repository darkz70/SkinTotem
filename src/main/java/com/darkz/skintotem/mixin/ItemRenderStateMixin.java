package com.darkz.skintotem.mixin;

//? if >=1.21.4 {
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.utils.mixin.ItemRenderStateWithStack;

import org.jetbrains.annotations.Nullable;

//? if >=1.21.9 {
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
 //?}

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemRenderState.class)
public class ItemRenderStateMixin implements ItemRenderStateWithStack {

	//? if <=1.21.4 {
	/*@Shadow
	ModelTransformationMode modelTransformationMode;
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
	private void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, int outlineColor, CallbackInfo ci) {
		this.renderDoll(matrices, light, overlay, outlineColor, null, ci);
	}
	//?} else {
	/*@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay, CallbackInfo ci) {
		this.renderDoll(matrices, light, overlay, 0, provider, ci);
	}
	*///?}

	@Unique
	private void renderDoll(MatrixStack matrices, int light, int overlay, @SuppressWarnings("all") int outlineColor, @Nullable VertexConsumerProvider provider, CallbackInfo ci) {
		DollRenderContext context = DollRenderContext.of(/*? if <=1.21.4 {*//*this.modelTransformationMode*//*?} else {*/ this.displayContext /*?}*/);

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