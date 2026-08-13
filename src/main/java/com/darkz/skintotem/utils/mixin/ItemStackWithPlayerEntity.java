package com.darkz.skintotem.utils.mixin;

import net.minecraft.client.player.AbstractClientPlayer;

public interface ItemStackWithPlayerEntity {

	void skinTotem$setPlayerEntity(AbstractClientPlayer player);

	AbstractClientPlayer skinTotem$getPlayerEntity();
}
