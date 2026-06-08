package com.darkz.skintotem.mixin.sodium;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.client.model.ModelPart;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkz.skintotem.model.base.*;

@Pseudo
@Mixin(ModelPart.class)
public class ModelPartMixinMixin {

	@Dynamic
	@TargetHandler(
			mixin = "me.jellysquid.mods.sodium.mixin.features.render.entity.ModelPartMixin",
			name = "onRender",
			prefix = "handler"
	)
	@Inject(at = @At("HEAD"), method = "@MixinSquared:Handler", cancellable = true, remap = false, require = 0)
	private void helloSodium(PoseStack matrices, VertexConsumer vertices, int light, int overlay, /*? if =1.20.1 {*/ /*float red, float green, float blue, float alpha, *//*?} else {*/ int color, /*?}*/ CallbackInfo a, CallbackInfo b) {
		ModelPart modelPart = (ModelPart) (Object) this;
		if (!(modelPart instanceof MModel)) {
			return;
		}
		b.cancel();
	}

}