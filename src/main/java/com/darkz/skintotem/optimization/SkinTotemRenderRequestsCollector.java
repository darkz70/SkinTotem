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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

@ExtensionMethod(MatrixStackEntryExtension.class)
public class SkinTotemRenderRequestsCollector {

	private static final SkinTotemRenderRequestsCollector INSTANCE = new SkinTotemRenderRequestsCollector();

	public static SkinTotemRenderRequestsCollector getInstance() {
		return INSTANCE;
	}

	private final MatrixStack matrices = new MatrixStack();
	private final List<SkinTotemRenderRequest> requests = new ArrayList<>();
	private final SkinTotemRenderProperties tempProperties = new SkinTotemRenderProperties();

	private SkinTotemRenderRequestsCollector() {

	}

	public void requestRender(MatrixStack matrices, SkinTotemData data, AbstractClientPlayerEntity holdingPlayer, DollRenderContext context, int light, int overlay, int outlineColor, @Nullable VertexConsumerProvider provider) {
		MatrixStack.Entry entry = matrices.peek();
		this.requests.add(new SkinTotemRenderRequest(/*? if >=1.21 {*/ entry.copy() /*?} else {*/ /*new MatrixStack.Entry(new Matrix4f(entry.getPositionMatrix()), new Matrix3f(entry.getNormalMatrix())) *//*?}*/, data, data.getRenderProperties().copy(), holdingPlayer, context, light, overlay, outlineColor, provider));
	}

	public void render() {
		LockableAtlasTexture atlasTexture = SkinTotemAtlasManager.getNullableAtlasTexture();
		if (atlasTexture == null) {
			SkinTotemClient.LOGGER.error("Game tried to render doll model requests, but atlas not initialized yet!");
			return;
		}
		atlasTexture.setLocked(true);

		Immediate mainProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		OutlineVertexConsumerProvider outlineProvider = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();

		for (SkinTotemRenderRequest request : this.requests) {
			this.renderRequest(request, request.provider() == null ? mainProvider : request.provider(), outlineProvider);
		}

		this.requests.clear();
		mainProvider.drawCurrentLayer();
		// We should draw this before unlocking, to make sure that atlas won't be changed earlier than the draw call
		atlasTexture.setLocked(false);
	}

	private void renderRequest(SkinTotemRenderRequest request, VertexConsumerProvider mainProvider, @SuppressWarnings("unused") OutlineVertexConsumerProvider outlineProvider) {
		this.matrices.push();
		this.matrices.peek().copyFrom(request.copyPeek());

		SkinTotemData data = request.data();
		this.tempProperties.copyFrom(data.getRenderProperties());

		data.getRenderProperties().copyFrom(request.renderProperties());
		data.clearFrameModel();
		SkinTotemModel modelToRender = data.getModelToRender();
		if (modelToRender == null) {
			this.matrices.pop();
			return;
		}
		modelToRender.resetPartsVisibility();
		data.getRenderProperties().applyToModel(modelToRender);

		SkinTotemRenderer.renderDoll(this.matrices, data, request.holdingPlayer(), request.context(), mainProvider, request.light(), request.overlay());
		//? if >=1.21.9 {
		int argb = request.outlineColor();
		if (argb != 0) {
			outlineProvider.setColor(argb);
			SkinTotemRenderer.renderDoll(this.matrices, data, request.holdingPlayer(), request.context(), outlineProvider, request.light(), request.overlay());
		}//?}

		data.getRenderProperties().copyFrom(this.tempProperties);

		this.matrices.pop();
	}

}
