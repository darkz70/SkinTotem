package com.darkz.skintotem.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;

import com.darkz.skintotem.utils.mixin.ItemStackWithPlayerEntity;


@Mixin(ItemStack.class)
public class ItemStackWithPlayerEntityMixin implements ItemStackWithPlayerEntity {

	@Unique
	private AbstractClientPlayerEntity player;

	@Override
	public void skinTotem$setPlayerEntity(AbstractClientPlayerEntity player) {
		this.player = player;
	}

	@Override
	public AbstractClientPlayerEntity skinTotem$getPlayerEntity() {
		return this.player;
	}
}
