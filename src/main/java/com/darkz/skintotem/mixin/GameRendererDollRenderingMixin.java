package com.darkz.skintotem.mixin;

import com.darkz.skintotem.optimization.TotemDollRenderRequestsCollector;
import com.darkz.skintotem.thing.ThingMarks;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class GameRendererDollRenderingMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"), method = "renderLevel")
	private void beforeDollRendering(CallbackInfo ci) {
		ThingMarks.WORLD_RENDERING.get().setMarked(true);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V", ordinal = 0), method = "renderLevel")
	private void afterDollRendering(CallbackInfo ci) {
		TotemDollRenderRequestsCollector.getInstance().render();
		ThingMarks.WORLD_RENDERING.get().setMarked(false);
	}

}
