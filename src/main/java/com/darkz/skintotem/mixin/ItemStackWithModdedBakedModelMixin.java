package com.darkz.skintotem.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.darkz.skintotem.utils.mixin.ItemStackWithModdedBakedModel;

@Mixin(ItemStack.class)
public class ItemStackWithModdedBakedModelMixin implements ItemStackWithModdedBakedModel {

	@Unique
	private boolean st$modded = false;

	@Override
	public void st$setModdedModel(boolean modded) {
		this.st$modded = modded;
	}

	@Override
	public boolean st$isModdedModel() {
		return st$modded;
	}

	@Inject(at = @At("RETURN"), method = "copy")
	private void markItemStack(CallbackInfoReturnable<ItemStack> cir) {
		((ItemStackWithModdedBakedModel) cir.getReturnValue()).st$setModdedModel(this.st$isModdedModel());
		this.st$modded = false;
	}
}
