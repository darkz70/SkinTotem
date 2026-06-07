package com.darkz.skintotem.doll.data;

//? if >=1.21.9 {
import net.minecraft.entity.player.AbstractClientPlayerEntity;
//?} else {
/*import net.minecraft.client.entity.AbstractClientPlayerEntity;*/
//?}
import net.minecraft.util.Identifier;
import com.darkz.skintotem.doll.TotemDollType;

public class TotemDollData {

	private final TotemDollType type;
	private final AbstractClientPlayerEntity player;
	private final TotemDollSprites sprites;

	public TotemDollData(TotemDollType type, AbstractClientPlayerEntity player) {
		this.type = type;
		this.player = player;
		this.sprites = new TotemDollSprites();
	}

	public TotemDollType getType() {
		return type;
	}

	public AbstractClientPlayerEntity getPlayer() {
		return player;
	}

	public TotemDollSprites getSprites() {
		return sprites;
	}

	public TotemDollSprites getStandardSprites() {
		return sprites;
	}

	public void setSprites(TotemDollSprites sprites) {
		this.sprites.copyFrom(sprites);
	}
}
