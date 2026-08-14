package com.darkz.skintotem.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;

import com.darkz.skintotem.utils.mixin.ItemStackWithPlayerEntity;


@Mixin(ItemStack.class)
public class ItemStackWithPlayerEntityMixin implements ItemStackWithPlayerEntity {

	@Unique
	private AbstractClientPlayer st$player;

	@Override
	public void st$setPlayerEntity(AbstractClientPlayer player) {
		this.st$player = player;
	}

	@Override
	public AbstractClientPlayer st$getPlayerEntity() {
		return this.st$player;
	}
}
