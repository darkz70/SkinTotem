package com.darkz.skintotem.utils.mixin;

import net.minecraft.world.item.ItemStack;

public interface ItemStackRenderStateWithStack {

	void myTotemDoll$setStack(ItemStack stack);

	void myTotemDoll$shouldClear(boolean bl);

}
