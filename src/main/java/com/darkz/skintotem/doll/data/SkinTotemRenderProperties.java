package com.darkz.skintotem.doll.data;

import it.unimi.dsi.fastutil.ints.*;
import java.util.*;
import java.util.function.Consumer;
import lombok.*;
import com.darkz.skintotem.doll.model.SkinTotemModel;
import com.darkz.skintotem.doll.renderer.DollRenderContext;
import com.darkz.skintotem.model.base.*;
import com.darkz.skintotem.model.bb.manager.BlockBenchModelManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.*;

public class SkinTotemRenderProperties {

	public boolean isSlim() { return slim; }
	public void setSlim(boolean slim) { this.slim = slim; }
	public String getNickname() { return nickname; }
	public void setNickname(String nickname) { this.nickname = nickname; }
	public DollRenderContext getRenderContext() { return renderContext; }
	public void setRenderContext(DollRenderContext renderContext) { this.renderContext = renderContext; }
	public String[] getDisabledParts() { return disabledParts; }
	public void setDisabledParts(String[] disabledParts) { this.disabledParts = disabledParts; }
	public String[] getEnabledParts() { return enabledParts; }
	public void setEnabledParts(String[] enabledParts) { this.enabledParts = enabledParts; }
	public MModel getStandardMModel() { return standardMModel; }
	public void setStandardMModel(MModel standardMModel) { this.standardMModel = standardMModel; }
	public MModel getFrameMModel() { return frameMModel; }
	public void setFrameMModel(MModel frameMModel) { this.frameMModel = frameMModel; }
	public SkinTotemSprites getStandardSprites() { return standardSprites; }
	public void setStandardSprites(SkinTotemSprites standardSprites) { this.standardSprites = standardSprites; }
	public SkinTotemSprites getFrameSprites() { return frameSprites; }
	public void setFrameSprites(SkinTotemSprites frameSprites) { this.frameSprites = frameSprites; }

	private boolean slim;
	@Nullable
	private String nickname;
	@Nullable
	private DollRenderContext renderContext;
	@NotNull
	private String[] disabledParts = new String[0];
	@NotNull
	private String[] enabledParts = new String[0];
	@Nullable
	private MModel standardMModel;
	@Nullable
	private MModel frameMModel;
	@NotNull
	private SkinTotemSprites standardSprites;
	@Nullable
	private SkinTotemSprites frameSprites;
	@NotNull
	private final Map<ResourceLocation, MModel> cachedFrameMModels = new HashMap<>();
	@NotNull
	private final Int2ObjectMap<SkinTotemSprites> cachedFrameTextures = new Int2ObjectArrayMap<>();

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SkinTotemRenderProperties that)) return false;
		return this.isSlim() == that.isSlim() && this.getRenderContext() == that.getRenderContext() && Objects.equals(this.getNickname(), that.getNickname()) && Objects.deepEquals(this.getDisabledParts(), that.getDisabledParts()) && Objects.deepEquals(this.getEnabledParts(), that.getEnabledParts()) && Objects.equals(this.getFrameMModel(), that.getFrameMModel()) && Objects.equals(this.getStandardMModel(), that.getStandardMModel());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.isSlim(), this.getRenderContext(), this.getNickname(), Arrays.hashCode(this.getDisabledParts()), Arrays.hashCode(this.getEnabledParts()), this.getFrameMModel(), this.getStandardMModel());
	}

	public SkinTotemModel createStandardModel() {
		return new SkinTotemModel(this.standardMModel, this.isSlim());
	}

	public SkinTotemModel createFrameModel() {
		return new SkinTotemModel(this.frameMModel, this.isSlim());
	}

	public void consumeFrameMModel(@NotNull ResourceLocation id, Consumer<MModel> set) {
		MModel model = this.cachedFrameMModels.get(id);
		if (model == null) {
			BlockBenchModelManager.getModelAsyncAsResponse(id, (response) -> {
				if (!response.isEmpty()) {
					MModel tempMModel = response.value();
					this.cachedFrameMModels.put(id, tempMModel);
					set.accept(tempMModel);
				}
			});
			return;
		}
		set.accept(model);
	}

	//? if >=1.21 {
	public void setFrameSprites(ResourceLocation skinTexture, ResourceLocation capeTexture, ResourceLocation elytraTexture, boolean slim, boolean remapCape) {
		int hash = Objects.hash(skinTexture, capeTexture, elytraTexture, slim);
		SkinTotemSprites cachedSprites = this.cachedFrameTextures.get(hash);
		if (cachedSprites == null) {
			SkinTotemSprites sprites = SkinTotemSprites.of(skinTexture, capeTexture, elytraTexture, slim, remapCape);
			this.cachedFrameTextures.put(hash, sprites);
			this.setFrameSprites(sprites);
		}
		this.setFrameSprites(cachedSprites);
	}
	//?}

	public void disable(MModelCollection collection) {
		if (!collection.setVisible(false)) {
			return;
		}
		String[] created = Arrays.copyOf(this.disabledParts, this.disabledParts.length + 1);
		created[created.length-1] = collection.getId();
		this.disabledParts = created;
	}

	public void enable(MModelCollection collection) {
		if (!collection.setVisible(true)) {
			return;
		}
		String[] created = Arrays.copyOf(this.enabledParts, this.enabledParts.length + 1);
		created[created.length-1] = collection.getId();
		this.enabledParts = created;
	}

	public void refresh() {
		this.refresh(this.standardSprites);
	}

	public void refresh(SkinTotemSprites sprites) {
		this.standardSprites = sprites;
		this.enabledParts    = new String[0];
		this.disabledParts   = new String[0];
		this.slim            = sprites.getArmsType().isSlim();
		this.frameMModel     = null;
		this.renderContext   = null;
	}

	public void clearCachedFrameMModels() {
		this.cachedFrameMModels.clear();
		this.frameMModel = null;
	}

	public void applyToModel(SkinTotemModel model) {
		for (String part : this.disabledParts) {
			MModelCollection collection = model.getCollectionOfPart(part);
			if (collection == null) {
				continue;
			}
			SkinTotemModel.disableIfPresent(collection);
		}
		for (String part : this.enabledParts) {
			MModelCollection collection = model.getCollectionOfPart(part);
			if (collection == null) {
				continue;
			}
			SkinTotemModel.enableIfPresent(collection);
		}
		model.setSlim(this.isSlim());
	}

	public SkinTotemRenderProperties copyFrom(SkinTotemRenderProperties properties) {
		this.setSlim(properties.isSlim());
		this.setRenderContext(properties.getRenderContext());
		this.setNickname(properties.getNickname());
		this.setDisabledParts(properties.getDisabledParts().clone());
		this.setEnabledParts(properties.getEnabledParts().clone());
		this.setStandardMModel(properties.getStandardMModel());
		this.setFrameMModel(properties.getFrameMModel());
		this.setStandardSprites(properties.getStandardSprites());
		this.setFrameSprites(properties.getFrameSprites());
		return this;
	}

	public SkinTotemRenderProperties copy() {
		return new SkinTotemRenderProperties().copyFrom(this);
	}
}
