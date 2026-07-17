package com.darkz.skintotem.doll.renderer.special;

//? if >=1.21.6 {

import java.util.*;
import lombok.*;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.doll.data.SkinTotemRenderProperties;
import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.utils.LightningUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ExtensionMethod(ItemStackExtension.class)
public class SkinTotemGuiElementRenderer extends SpecialGuiElementRenderer<SkinTotemRenderState> {

	public static final Map<SkinTotemRenderProperties, SkinTotemGuiElementRenderer> PROPERTIES_RENDERERS = new HashMap<>();

	private boolean active;

	public SkinTotemGuiElementRenderer(Immediate vertexConsumers) {
		super(vertexConsumers);
	}

	@NotNull
	public static SkinTotemGuiElementRenderer getRenderer(SkinTotemRenderProperties renderProperties, Immediate immediate) {
		SkinTotemGuiElementRenderer renderer = PROPERTIES_RENDERERS.get(renderProperties.copy());
		if (renderer == null) {
			SkinTotemGuiElementRenderer createdRenderer = new SkinTotemGuiElementRenderer(immediate);
			PROPERTIES_RENDERERS.put(renderProperties, createdRenderer);
			return createdRenderer;
		}

		return renderer;
	}

	public static void closeTotemRenderers() {
		PROPERTIES_RENDERERS.values().forEach(SkinTotemGuiElementRenderer::close);
	}

	public static void clearUnusedRenderers() {
		int all = PROPERTIES_RENDERERS.size();
		PROPERTIES_RENDERERS.entrySet().removeIf((entry) -> {
			SkinTotemGuiElementRenderer renderer = entry.getValue();
			if (!renderer.isActive()) {
				renderer.close();
				return true;
			}
			renderer.setActive(false);
			return false;
		});
		int cleared = all - PROPERTIES_RENDERERS.size();
		if (SkinTotemConfig.getInstance().isDebugLogEnabled() && cleared != 0) {
			SkinTotemClient.LOGGER.info("Removed Inactive Totem Doll Renderers: {}", cleared);
		}
	}

	@Override
	protected void render(SkinTotemRenderState state, MatrixStack matrices) {
		if (state.renderContext() == DollRenderContext.D_PREVIEW && state.data() != null) {
			SkinTotemRenderer.renderDataPreview(matrices, this.vertexConsumers, this.vertexConsumers::draw, state.size() + 1, state.data());
		} else if (state.stack() != null) {
			LightningUtils.disable3dLighting();
			matrices.push();
			matrices.scale(16F, -16F, -16F);
			SkinTotemRenderer.renderDoll(matrices, state.stack(), state.renderContext(), this.vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV);
			matrices.pop();
			this.vertexConsumers.draw();
			LightningUtils.enable3dLighting();

			if (state.stack().hasModdedModel()) {
				state.stack().setModdedModel(false);
			}
		}
	}

	@Override
	public Class<SkinTotemRenderState> getElementClass() {
		return SkinTotemRenderState.class;
	}

	@Override
	protected String getName() {
		return "%s-doll-special-gui-renderer".formatted(SkinTotem.MOD_ID);
	}

	@Override
	protected float getYOffset(int height, int windowScaleFactor) {
		return height / 2F;
	}
}
//?}
