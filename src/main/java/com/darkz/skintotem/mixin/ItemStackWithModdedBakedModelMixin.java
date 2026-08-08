package com.darkz.skintotem.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.darkz.skintotem.utils.mixin.ItemStackWithModdedBakedModel;

@Mixin(ItemStack.class)
public class ItemStackWithModdedBakedModelMixin implements ItemStackWithModdedBakedModel {

	@Unique
	private boolean modded = false;

	@Override
	public void skinTotem$setModdedModel(boolean modded) {
		this.modded = modded;
	}

	@Override
	public boolean skinTotem$isModdedModel() {
		return modded;
	}

	//? if <=1.21.4 {
	/*@Inject(at = @At("RETURN"), method = "copy")
	private void markItemStack(CallbackInfoReturnable<ItemStack> cir) {
		((ItemStackWithModdedBakedModel) cir.getReturnValue()).skinTotem$setModdedModel(this.skinTotem$isModdedModel());
		this.modded = false;
	}
	*///?} else if <=1.21.5 {
	/*@Inject(at = @At("RETURN"), method = "copy()Lnet/minecraft/item/ItemStack;")
	private void markItemStack(CallbackInfoReturnable<ItemStack> cir) {
		((ItemStackWithModdedBakedModel) cir.getReturnValue()).skinTotem$setModdedModel(this.skinTotem$isModdedModel());
		this.modded = false;
	}
	*///?}
}
