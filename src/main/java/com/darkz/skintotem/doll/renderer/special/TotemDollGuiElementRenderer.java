package com.darkz.skintotem.doll.renderer.special;

//? if >=1.21.6 {

import java.util.*;
import lombok.*;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.config.SkinTotemModConfig;
import com.darkz.skintotem.doll.data.TotemDollRenderProperties;
import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.utils.LightningUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ExtensionMethod(ItemStackExtension.class)
public class TotemDollGuiElementRenderer extends SpecialGuiElementRenderer<TotemDollRenderState> {

	public static final Map<TotemDollRenderProperties, TotemDollGuiElementRenderer> PROPERTIES_RENDERERS = new HashMap<>();

	private boolean active;

	public TotemDollGuiElementRenderer(Immediate vertexConsumers) {
		super(vertexConsumers);
	}

	@NotNull
	public static TotemDollGuiElementRenderer getRenderer(TotemDollRenderProperties renderProperties, Immediate immediate) {
		TotemDollGuiElementRenderer renderer = PROPERTIES_RENDERERS.get(renderProperties.copy());
		if (renderer == null) {
			TotemDollGuiElementRenderer createdRenderer = new TotemDollGuiElementRenderer(immediate);
			PROPERTIES_RENDERERS.put(renderProperties, createdRenderer);
			return createdRenderer;
		}

		return renderer;
	}

	public static void closeTotemRenderers() {
		PROPERTIES_RENDERERS.values().forEach(TotemDollGuiElementRenderer::close);
	}

	public static void clearUnusedRenderers() {
		int all = PROPERTIES_RENDERERS.size();
		PROPERTIES_RENDERERS.entrySet().removeIf((entry) -> {
			TotemDollGuiElementRenderer renderer = entry.getValue();
			if (!renderer.isActive()) {
				renderer.close();
				return true;
			}
			renderer.setActive(false);
			return false;
		});
		int cleared = all - PROPERTIES_RENDERERS.size();
		if (SkinTotemModConfig.getInstance().isDebugLogEnabled() && cleared != 0) {
			SkinTotemModClient.LOGGER.info("Removed Inactive Totem Doll Renderers: {}", cleared);
		}
	}

	@Override
	protected void render(TotemDollRenderState state, PoseStack matrices) {
		if (state.renderContext() == DollRenderContext.D_PREVIEW && state.data() != null) {
			TotemDollRenderer.renderDataPreview(matrices, this.vertexConsumers, this.vertexConsumers::draw, state.size() + 1, state.data());
		} else if (state.stack() != null) {
			LightningUtils.disable3dLighting();
			matrices.push();
			matrices.scale(16F, -16F, -16F);
			TotemDollRenderer.renderDoll(matrices, state.stack(), state.renderContext(), this.vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV);
			matrices.pop();
			this.vertexConsumers.draw();
			LightningUtils.enable3dLighting();

			if (state.stack().hasModdedModel()) {
				state.stack().setModdedModel(false);
			}
		}
	}

	@Override
	public Class<TotemDollRenderState> getElementClass() {
		return TotemDollRenderState.class;
	}

	@Override
	protected String getName() {
		return "%s-doll-special-gui-renderer".formatted(SkinTotemMod.MOD_ID);
	}

	@Override
	protected float getYOffset(int height, int windowScaleFactor) {
		return height / 2F;
	}
}
//?}
