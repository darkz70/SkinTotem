package com.darkz.skintotem.mixin;

import com.darkz.skintotem.optimization.TotemDollRenderRequestsCollector;
import com.darkz.skintotem.thing.ThingMarks;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class GameRendererDollRenderingMixin {

	//? if >=1.21.9 {
	@Inject(at = @At("HEAD"), method = "fillEntityRenderStates")
	private void beforeDollRendering(CallbackInfo ci) {
		ThingMarks.WORLD_RENDERING.get().setMarked(true);
	}

	@Inject(at = @At("TAIL"), method = "fillEntityRenderStates")
	private void afterDollRendering(CallbackInfo ci) {
		TotemDollRenderRequestsCollector.getInstance().render();
		ThingMarks.WORLD_RENDERING.get().setMarked(false);
	}
	//?} elif >=1.21.2 && <=1.21.8 {
	/*@Inject(at = @At("HEAD"), method = "renderEntities")
	private void beforeDollRendering(CallbackInfo ci) {
		ThingMarks.WORLD_RENDERING.get().setMarked(true);
	}

	@Inject(at = @At("TAIL"), method = "renderEntities")
	private void afterDollRendering(CallbackInfo ci) {
		TotemDollRenderRequestsCollector.getInstance().render();
		ThingMarks.WORLD_RENDERING.get().setMarked(false);
	}
	*///?} else {
	/*@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientLevel;getEntities()Ljava/lang/Iterable;"), method = "render")
	private void beforeDollRendering(CallbackInfo ci) {
		ThingMarks.WORLD_RENDERING.get().setMarked(true);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/MultiBufferSource$Immediate;drawCurrentLayer()V", ordinal = 0), method = "render")
	private void afterDollRendering(CallbackInfo ci) {
		TotemDollRenderRequestsCollector.getInstance().render();
		ThingMarks.WORLD_RENDERING.get().setMarked(false);
	}
	*///?}

}
