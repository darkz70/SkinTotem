package com.darkz.skintotem.utils.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public interface ItemStackWithPlayerEntity {

	void myTotemDoll$setPlayerEntity(AbstractClientPlayerEntity player);

	AbstractClientPlayerEntity myTotemDoll$getPlayerEntity();
}
