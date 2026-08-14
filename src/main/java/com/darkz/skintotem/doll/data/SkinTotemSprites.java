package com.darkz.skintotem.doll.data;

import lombok.*;
import com.darkz.skintotem.atlas.*;
import com.darkz.skintotem.atlas.manager.*;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.config.totem.SkinTotemArmsType;

import org.jetbrains.annotations.*;
import static com.darkz.skintotem.atlas.manager.SkinTotemAtlasSpriteManager.ELYTRA_SPRITE;
import static com.darkz.skintotem.atlas.manager.SkinTotemAtlasSpriteManager.STEVE_SKIN_SPRITE;


@Getter
@Setter
@AllArgsConstructor
public class SkinTotemSprites {

	@NotNull
	private LoadingState state = LoadingState.NOT_DOWNLOADED;

	@Nullable
	private AtlasSprite skinSprite;
	@Nullable
	private AtlasSprite capeSprite;
	@Nullable
	private AtlasSprite elytraSprite;

	private SkinTotemArmsType standardArmsType;
	private SkinTotemArmsType armsType;

	public SkinTotemSprites(@Nullable AtlasSprite skinSprite, @Nullable AtlasSprite capeSprite, @Nullable AtlasSprite elytraSprite, SkinTotemArmsType armsType) {
		this.skinSprite   = skinSprite;
		this.capeSprite   = capeSprite;
		this.elytraSprite = elytraSprite;
		this.armsType     = armsType;
	}

	public static SkinTotemSprites create() {
		return new SkinTotemSprites(null, null, null, SkinTotemArmsType.WIDE);
	}


	public static SkinTotemSprites of(ResourceLocation skinTexture, ResourceLocation capeTexture, ResourceLocation elytraTexture, boolean slim, boolean remapCape) {
		SkinTotemSprites totemDollSprites = new SkinTotemSprites(null, null, null, SkinTotemArmsType.of(slim));

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

	public void setStandardArmsType(SkinTotemArmsType standardArmsType) {
		this.armsType = standardArmsType;
		this.standardArmsType = standardArmsType;
	}

	public SkinTotemArmsType getArmsType() {
		return this.armsType == null ?
				this.standardArmsType == null ?
						SkinTotemArmsType.WIDE
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

	public SkinTotemSprites copy() {
		SkinTotemSprites totemDollSprites = new SkinTotemSprites(this.skinSprite, this.capeSprite, this.elytraSprite, this.armsType);
		totemDollSprites.setState(this.state);
		return totemDollSprites;
	}
}
