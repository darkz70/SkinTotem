package com.darkz.skintotem.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;

import com.darkz.skintotem.utils.mixin.ItemStackWithPlayerEntity;


@Mixin(ItemStack.class)
public class ItemStackWithPlayerEntityMixin implements ItemStackWithPlayerEntity {

	@Unique
	private AbstractClientPlayer skinTotem$player;

	@Override
	public void skinTotem$setPlayerEntity(AbstractClientPlayer player) {
		this.skinTotem$player = player;
	}

	@Override
	public AbstractClientPlayer skinTotem$getPlayerEntity() {
		return this.skinTotem$player;
	}
}
