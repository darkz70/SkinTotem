package com.darkz.skintotem.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.darkz.skintotem.utils.mixin.ItemStackWithdedBakedModel;

@Mixin(ItemStack.class)
public class ItemStackWithdedBakedModelMixin implements ItemStackWithdedBakedModel {

	@Unique
	private boolean modded = false;

	@Override
	public void skinTotem$setdedModel(boolean modded) {
		this.modded = modded;
	}

	@Override
	public boolean skinTotem$isdedModel() {
		return modded;
	}

	//? if <=1.21.5 {
	/*@Inject(at = @At("RETURN"), method = /^? if <=1.21.4 {^/ /^"copy" ^//^?} else {^/ "copy()Lnet/minecraft/item/ItemStack;" /^?}^/)
	private void markItemStack(CallbackInfoReturnable<ItemStack> cir) {
		((ItemStackWithdedBakedModel) cir.getReturnValue()).skinTotem$setdedModel(this.skinTotem$isdedModel());
		this.modded = false;
	}
	*///?}
}
