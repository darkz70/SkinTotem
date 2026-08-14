package com.darkz.skintotem.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.darkz.skintotem.utils.mixin.ItemStackWithModdedBakedModel;

@Mixin(ItemStack.class)
public class ItemStackWithModdedBakedModelMixin implements ItemStackWithModdedBakedModel {

	@Unique
	private boolean mySkinTotem$modded = false;

	@Override
	public void mySkinTotem$setModdedModel(boolean modded) {
		this.mySkinTotem$modded = modded;
	}

	@Override
	public boolean mySkinTotem$isModdedModel() {
		return mySkinTotem$modded;
	}

	@Inject(at = @At("RETURN"), method = "copy")
	private void markItemStack(CallbackInfoReturnable<ItemStack> cir) {
		((ItemStackWithModdedBakedModel) cir.getReturnValue()).mySkinTotem$setModdedModel(this.mySkinTotem$isModdedModel());
		this.mySkinTotem$modded = false;
	}
}
