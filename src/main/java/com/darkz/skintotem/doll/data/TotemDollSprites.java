package com.darkz.skintotem.doll.data;

import lombok.*;
import com.darkz.skintotem.atlas.*;
import com.darkz.skintotem.atlas.manager.*;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.config.totem.TotemDollArmsType;

import org.jetbrains.annotations.*;
import static com.darkz.skintotem.atlas.manager.SkinTotemAtlasSpriteManager.ELYTRA_SPRITE;
import static com.darkz.skintotem.atlas.manager.SkinTotemAtlasSpriteManager.STEVE_SKIN_SPRITE;


@Getter
@Setter
@AllArgsConstructor
public class TotemDollSprites {

	@NotNull
	private LoadingState state = LoadingState.NOT_DOWNLOADED;

	@Nullable
	private AtlasSprite skinSprite;
	@Nullable
	private AtlasSprite capeSprite;
	@Nullable
	private AtlasSprite elytraSprite;

	private TotemDollArmsType standardArmsType;
	private TotemDollArmsType armsType;

	public TotemDollSprites(@Nullable AtlasSprite skinSprite, @Nullable AtlasSprite capeSprite, @Nullable AtlasSprite elytraSprite, TotemDollArmsType armsType) {
		this.skinSprite   = skinSprite;
		this.capeSprite   = capeSprite;
		this.elytraSprite = elytraSprite;
		this.armsType     = armsType;
	}

	public static TotemDollSprites create() {
		return new TotemDollSprites(null, null, null, TotemDollArmsType.WIDE);
	}


	public static TotemDollSprites of(ResourceLocation skinTexture, ResourceLocation capeTexture, ResourceLocation elytraTexture, boolean slim, boolean remapCape) {
		TotemDollSprites totemDollSprites = new TotemDollSprites(null, null, null, TotemDollArmsType.of(slim));

		if (skinTexture != null) {
			SkinTotemAtlasSpriteManager.registerSpecialSkinSprite(skinTexture, false, totemDollSprites::setSkinSprite);
		}

		if (capeTexture != null) {
			if (remapCape) {
				RemappedAtlasSprite capeSprite = RemappedAtlasSprite.ofResource(capeTexture);
				SkinTotemAtlasSpriteManager.registerSpecialRemappedSprite(capeSprite);
				totemDollSprites.setCapeSprite(capeSprite);
			} else {
				SkinTotemAtlasSpriteManager.registerSpecialSkinSprite(capeTexture, false, totemDollSprites::setCapeSprite);
			}
		}

		if (elytraTexture != null) {
			SkinTotemAtlasSpriteManager.registerSpecialSkinSprite(elytraTexture, false, totemDollSprites::setElytraSprite);
		}

		SkinTotemAtlasManager.stitchAndUpdate(SkinTotemAtlasSpriteManager.getSprites(), () -> {
			totemDollSprites.setState(LoadingState.DOWNLOADED);
		});

		return totemDollSprites;
	}

	public void setStandardArmsType(TotemDollArmsType standardArmsType) {
		this.armsType = standardArmsType;
		this.standardArmsType = standardArmsType;
	}

	public TotemDollArmsType getArmsType() {
		return this.armsType == null ?
				this.standardArmsType == null ?
						TotemDollArmsType.WIDE
						:
						this.standardArmsType
				:
				this.armsType;
	}

	public AtlasSprite getSkinSprite() {
		return this.skinSprite == null || !this.skinSprite.isUploaded() || this.state != LoadingState.DOWNLOADED ? STEVE_SKIN_SPRITE : this.skinSprite;
	}

	public AtlasSprite getElytraSprite() {
		AtlasSprite capeSprite = this.getCapeSprite();
		if (capeSprite != null && capeSprite.isUploaded()) {
			return capeSprite;
		}
		AtlasSprite elytraSprite = this.elytraSprite;
		if (elytraSprite != null && elytraSprite.isUploaded()) {
			return elytraSprite;
		}
		return ELYTRA_SPRITE;
	}

	public void destroy() {
		this.setState(LoadingState.DESTROYED);

		AtlasSprite skinSprite = this.skinSprite;
		AtlasSprite capeSprite = this.capeSprite;
		AtlasSprite elytraSprite = this.elytraSprite;

		this.skinSprite   = null;
		this.capeSprite   = null;
		this.elytraSprite = null;

		if (skinSprite != null) {
			skinSprite.closeAndUnregisterAnyway();
		}

		if (capeSprite != null) {
			capeSprite.close();
		}

		if (elytraSprite != null) {
			elytraSprite.close();
		}
	}

	public boolean canStartDownloading() {
		return this.state == LoadingState.ERROR || this.state == LoadingState.NOT_DOWNLOADED;
	}

	public TotemDollSprites copy() {
		TotemDollSprites totemDollSprites = new TotemDollSprites(this.skinSprite, this.capeSprite, this.elytraSprite, this.armsType);
		totemDollSprites.setState(this.state);
		return totemDollSprites;
	}
}
