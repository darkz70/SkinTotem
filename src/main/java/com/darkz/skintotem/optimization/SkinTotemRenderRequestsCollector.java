package com.darkz.skintotem.optimization;

import java.util.*;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.atlas.LockableAtlasTexture;
import com.darkz.skintotem.atlas.manager.SkinTotemAtlasManager;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.doll.model.SkinTotemModel;
import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.extension.MatrixStackEntryExtension;
import com.darkz.skintotem.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.OutlineBufferSource;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

@ExtensionMethod(MatrixStackEntryExtension.class)
public class SkinTotemRenderRequestsCollector {

	private static final SkinTotemRenderRequestsCollector INSTANCE = new SkinTotemRenderRequestsCollector();

	public static SkinTotemRenderRequestsCollector getInstance() {
		return INSTANCE;
	}

	private final PoseStack matrices = new PoseStack();
	private final List<SkinTotemRenderRequest> requests = new ArrayList<>();
	private final SkinTotemRenderProperties tempProperties = new SkinTotemRenderProperties();

	private SkinTotemRenderRequestsCollector() {

	}

	public void requestRender(PoseStack matrices, SkinTotemData data, AbstractClientPlayer holdingPlayer, DollRenderContext context, int light, int overlay, int outlineColor, @Nullable MultiBufferSource provider) {
		PoseStack.Pose entry = matrices.last();
		this.requests.add(new SkinTotemRenderRequest( new PoseStack.Pose(new Matrix4f(entry.pose()), new Matrix3f(entry.normal())) , data, data.getRenderProperties().copy(), holdingPlayer, context, light, overlay, outlineColor, provider));
	}

	public void render() {
		LockableAtlasTexture atlasTexture = SkinTotemAtlasManager.getNullableAtlasTexture();
		if (atlasTexture == null) {
			SkinTotemClient.LOGGER.error("Game tried to render doll model requests, but atlas not initialized yet!");
			return;
		}
		atlasTexture.setLocked(true);

		BufferSource mainProvider = Minecraft.getInstance().renderBuffers().bufferSource();
		OutlineBufferSource outlineProvider = Minecraft.getInstance().renderBuffers().outlineBufferSource();

		for (SkinTotemRenderRequest request : this.requests) {
			this.renderRequest(request, request.provider() == null ? mainProvider : request.provider(), outlineProvider);
		}

		this.requests.clear();
		mainProvider.endLastBatch();
		// We should draw this before unlocking, to make sure that atlas won't be changed earlier than the draw call
		atlasTexture.setLocked(false);
	}

	private void renderRequest(SkinTotemRenderRequest request, MultiBufferSource mainProvider, @SuppressWarnings("unused") OutlineBufferSource outlineProvider) {
		this.matrices.pushPose();
		this.matrices.last().copyFrom(request.copyPeek());

		SkinTotemData data = request.data();
		this.tempProperties.copyFrom(data.getRenderProperties());

		data.getRenderProperties().copyFrom(request.renderProperties());
		data.clearFrameModel();
		SkinTotemModel modelToRender = data.getModelToRender();
		modelToRender.resetPartsVisibility();
		data.getRenderProperties().applyToModel(modelToRender);

		SkinTotemRenderer.renderDoll(this.matrices, data, request.holdingPlayer(), request.context(), mainProvider, request.light(), request.overlay());

		data.getRenderProperties().copyFrom(this.tempProperties);

		this.matrices.popPose();
	}

}
