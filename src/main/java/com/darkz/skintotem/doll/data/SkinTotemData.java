package com.darkz.skintotem.doll.data;

import lombok.*;
import com.darkz.skintotem.model.bb.manager.BlockBenchModelManager;
import net.minecraft.client.player.AbstractClientPlayer;

//? if >=1.21.9 {
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.util.AssetInfo.TextureAsset;
import java.util.Optional;
//?} elif >=1.21 {
/*import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.SkinTextures.*;
*///?}
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.doll.model.SkinTotemModel;
import com.darkz.skintotem.model.base.MModel;

import org.jetbrains.annotations.*;

@Getter
@Setter
public class SkinTotemData {

	private boolean shouldRecreateStandardModel;

	@Nullable
	private SkinTotemModel standardModel;
	@Nullable
	private SkinTotemModel frameModel;

	@NotNull
	private SkinTotemRenderProperties renderProperties = new SkinTotemRenderProperties();

	public SkinTotemData(@Nullable String nickname, @NotNull SkinTotemSprites sprites) {
		this.renderProperties = new SkinTotemRenderProperties();
		this.renderProperties.refresh(sprites);
		this.renderProperties.setNickname(nickname);
	}

	public SkinTotemData(@NotNull SkinTotemRenderProperties properties) {
		this.renderProperties.copyFrom(properties);
	}

	public static SkinTotemData create(@Nullable String nickname) {
		return new SkinTotemData(nickname, SkinTotemSprites.create());
	}

	public SkinTotemSprites getStandardSprites() {
		return this.renderProperties.getStandardSprites();
	}

	@Nullable
	public String getNickname() {
		return this.renderProperties.getNickname();
	}

	public void setStandardMModel(@NotNull ResourceLocation modelId) {
		BlockBenchModelManager.consumeModelById(modelId, this::setStandardMModel);
	}

	public void setStandardMModel(@Nullable MModel model) {
		this.renderProperties.setStandardMModel(model);
		if (model == null) {
			return;
		}
		this.standardModel = this.renderProperties.createStandardModel();
	}

	public void setFrameMModel(@NotNull ResourceLocation id) {
		this.renderProperties.consumeFrameMModel(id, this::setFrameMModel);
	}

	public void setFrameMModel(@Nullable MModel frameMModel) {
		this.renderProperties.setFrameMModel(frameMModel);
	}

	@Nullable
	private SkinTotemModel getFrameModelBasedOnFrameMModel() {
		//? if >=1.21 {
		if (this.renderProperties.getFrameMModel() != null) {
			if (this.frameModel == null || !this.frameModel.getMain().equals(this.renderProperties.getFrameMModel())) {
				return this.frameModel = this.renderProperties.createFrameModel();
			}
			return this.frameModel;
		}
		//?}
		return null;
	}

	public void clearAllFrameModelsCompletely() {
		this.clearFrameModel();
		this.renderProperties.clearCachedFrameMModels();
	}

	public void clearFrameModel() {
		if (this.frameModel != null) {
			this.frameModel.resetPartsVisibility();
			this.frameModel = null;
		}
	}

	public void clearFrameSprites() {
		this.renderProperties.setFrameSprites(null);
	}

	@NotNull
	public SkinTotemModel getModelToRender() {
		SkinTotemModel tempModel = this.getFrameModelBasedOnFrameMModel();
		if (tempModel != null) {
			return tempModel;
		}

		if (this.standardModel != null && !this.shouldRecreateStandardModel) {
			return this.standardModel;
		}

		//? if >=1.21 {
		this.setStandardMModel(SkinTotemModel.createDollModel());
		//?} else {
		/*this.setStandardMModel(SkinTotemModel.createDollModel());
		*///?}

		if (this.shouldRecreateStandardModel) {
			this.shouldRecreateStandardModel = false;
		}

		return this.standardModel;
	}

	@NotNull
	public SkinTotemSprites getSpritesToRender() {
		return this.renderProperties.getFrameSprites() == null ? this.renderProperties.getStandardSprites() : this.renderProperties.getFrameSprites();
	}

	public void setSprites(@NotNull SkinTotemSprites sprites) {
		this.renderProperties.setStandardSprites(sprites);
	}

	@SuppressWarnings("unused")
	public void setFrameSprites(@Nullable SkinTotemSprites frameSprites) {
		this.renderProperties.setFrameSprites(frameSprites);
	}

	public void setFrameSprites(@Nullable AbstractClientPlayer playerEntity) {
		if (playerEntity == null) {
			return;
		}

		//? if >=1.21.9 {
		SkinTextures skinTextures = playerEntity.getSkin();
		ResourceLocation skinTexture = skinTextures.body().texturePath();
		ResourceLocation capeTexture = Optional.of(skinTextures).map(SkinTextures::cape).map(TextureAsset::texturePath).orElse(null);
		ResourceLocation elytraTexture = Optional.of(skinTextures).map(SkinTextures::cape).map(TextureAsset::texturePath).orElse(null);
		boolean slim = skinTextures.model() == PlayerSkinType.SLIM;
		//?} elif >=1.21 {
		/*SkinTextures skinTextures = playerEntity.getSkinTextures();
		ResourceLocation skinTexture = skinTextures.texture();
		ResourceLocation capeTexture = skinTextures.capeTexture();
		ResourceLocation elytraTexture = skinTextures.elytraTexture();
		boolean slim = skinTextures.model() == SkinTextures.Model.SLIM;
		*///?} else {
		/*ResourceLocation skinTexture = playerEntity.getSkinTextureLocation();
		ResourceLocation capeTexture = playerEntity.getCloakTextureLocation();
		ResourceLocation elytraTexture = playerEntity.getElytraTextureLocation();
		boolean slim = playerEntity.getModelName().equalsIgnoreCase("slim");
		*///?}

		//? if >=1.21 {
		this.renderProperties.setFrameSprites(skinTexture, capeTexture, elytraTexture, slim, true);
		//?}
	}

	@NotNull
	public SkinTotemData copy() {
		return new SkinTotemData(this.renderProperties);
	}

	@NotNull
	public SkinTotemData refreshAndApplyRenderProperties() {
		return this.refreshRenderProperties().applyRenderProperties();
	}

	@NotNull
	public SkinTotemData refreshRenderProperties() {
		// Make sure it's cleared
		this.clearFrameModel();
		this.clearFrameSprites();
		SkinTotemModel model = this.getModelToRender();
		if (model != null) {
			model.resetPartsVisibility();
		}
		this.renderProperties.refresh();
		return this;
	}

	@NotNull
	public SkinTotemData applyRenderProperties() {
		SkinTotemModel modelToApply = this.getModelToRender();
		if (modelToApply != null) {
			this.renderProperties.applyToModel(modelToApply);
		}
		return this;
	}

	//? if >=1.21.6 {
	@NotNull
	public com.darkz.skintotem.doll.renderer.special.SkinTotemGuiElementRenderer getGuiRenderer(net.minecraft.client.render.VertexConsumerProvider.Immediate immediate) {
		return com.darkz.skintotem.doll.renderer.special.SkinTotemGuiElementRenderer.getRenderer(this.renderProperties, immediate);
	}
	//?}
}
