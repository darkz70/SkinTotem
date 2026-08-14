package com.darkz.skintotem.utils.mixin;

import net.minecraft.client.player.AbstractClientPlayer;

public interface ItemStackWithPlayerEntity {

	void st$setPlayerEntity(AbstractClientPlayer player);

	AbstractClientPlayer st$getPlayerEntity();
}
