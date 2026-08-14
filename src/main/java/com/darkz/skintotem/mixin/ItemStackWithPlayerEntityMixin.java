package com.darkz.skintotem.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;

import com.darkz.skintotem.utils.mixin.ItemStackWithPlayerEntity;


@Mixin(ItemStack.class)
public class ItemStackWithPlayerEntityMixin implements ItemStackWithPlayerEntity {

	@Unique
	private AbstractClientPlayer mySkinTotem$player;

	@Override
	public void mySkinTotem$setPlayerEntity(AbstractClientPlayer player) {
		this.mySkinTotem$player = player;
	}

	@Override
	public AbstractClientPlayer mySkinTotem$getPlayerEntity() {
		return this.mySkinTotem$player;
	}
}
