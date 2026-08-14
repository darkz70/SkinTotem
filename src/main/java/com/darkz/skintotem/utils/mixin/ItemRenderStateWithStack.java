package com.darkz.skintotem.utils.mixin;

import net.minecraft.world.item.ItemStack;

public interface ItemRenderStateWithStack {

	void st$setStack(ItemStack stack);

	void st$shouldClear(boolean bl);

}
