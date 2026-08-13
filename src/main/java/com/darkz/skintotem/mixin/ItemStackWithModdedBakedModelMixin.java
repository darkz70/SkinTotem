package com.darkz.skintotem.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.darkz.skintotem.utils.mixin.ItemStackWithModdedBakedModel;

@Mixin(ItemStack.class)
public class ItemStackWithModdedBakedModelMixin implements ItemStackWithModdedBakedModel {

	@Unique
	private boolean skinTotem$modded = false;

	@Override
	public void skinTotem$setModdedModel(boolean modded) {
		this.skinTotem$modded = modded;
	}

	@Override
	public boolean skinTotem$isModdedModel() {
		return skinTotem$modded;
	}

	@Inject(at = @At("RETURN"), method = "copy")
	private void markItemStack(CallbackInfoReturnable<ItemStack> cir) {
		((ItemStackWithModdedBakedModel) cir.getReturnValue()).skinTotem$setModdedModel(this.skinTotem$isModdedModel());
		this.skinTotem$modded = false;
	}
}
