package com.darkz.skintotem.utils.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public interface ItemStackWithPlayerEntity {

	void skinTotem$setPlayerEntity(AbstractClientPlayerEntity player);

	AbstractClientPlayerEntity skinTotem$getPlayerEntity();
}
