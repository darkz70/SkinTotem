package com.darkz.skintotem.mixin;

//? if >=1.21.9 {
import com.darkz.skintotem.utils.mixin.ItemStackRenderStateWithStack;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemPickupParticle.class)
public class ItemPickupParticleMixin {

	@Inject(at = @At("TAIL"), method = "<init>")
	private void markClear(ClientLevel world, EntityRenderState renderState, Entity collector, Vec3 velocity, CallbackInfo ci) {
		if (renderState instanceof ItemStackEntityRenderState state) {
			((ItemStackRenderStateWithStack) state.itemRenderState).myTotemDoll$shouldClear(false);
		}
	}

}
//?}
