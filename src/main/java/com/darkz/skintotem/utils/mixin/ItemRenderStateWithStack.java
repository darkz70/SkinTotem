package com.darkz.skintotem.utils.mixin;

import net.minecraft.item.ItemStack;

public interface ItemRenderStateWithStack {

	void skinTotem$setStack(ItemStack stack);

	void skinTotem$shouldClear(boolean bl);

}
