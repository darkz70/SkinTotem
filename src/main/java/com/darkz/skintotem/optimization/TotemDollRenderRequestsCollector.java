package com.darkz.skintotem.optimization;

import java.util.*;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.atlas.LockableAtlasTexture;
import com.darkz.skintotem.atlas.manager.SkinTotemModAtlasManager;
import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.doll.model.TotemDollModel;
import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.extension.PoseStackEntryExtension;
import com.darkz.skintotem.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

@ExtensionMethod(PoseStackEntryExtension.class)
public class TotemDollRenderRequestsCollector {

	private static final TotemDollRenderRequestsCollector INSTANCE = new TotemDollRenderRequestsCollector();

	public static TotemDollRenderRequestsCollector getInstance() {
		return INSTANCE;
	}

	private final PoseStack matrices = new PoseStack();
	private final List<TotemDollRenderRequest> requests = new ArrayList<>();
	private final TotemDollRenderProperties tempProperties = new TotemDollRenderProperties();

	private TotemDollRenderRequestsCollector() {

	}

	public void requestRender(PoseStack matrices, TotemDollData data, AbstractClientPlayer holdingPlayer, DollRenderContext context, int light, int overlay, int outlineColor, @Nullable MultiBufferSource provider) {
		PoseStack.Entry entry = matrices.peek();
		this.requests.add(new TotemDollRenderRequest(/*? if >=1.21 {*/ entry.copy() /*?} else {*/ /*new PoseStack.Entry(new Matrix4f(entry.getPositionMatrix()), new Matrix3f(entry.getNormalMatrix())) *//*?}*/, data, data.getRenderProperties().copy(), holdingPlayer, context, light, overlay, outlineColor, provider));
	}

	public void render() {
		LockableAtlasTexture atlasTexture = SkinTotemModAtlasManager.getNullableAtlasTexture();
		if (atlasTexture == null) {
			SkinTotemModClient.LOGGER.error("Game tried to render doll model requests, but atlas not initialized yet!");
			return;
		}
		atlasTexture.setLocked(true);

		Immediate mainProvider = Minecraft.getInstance().getBufferBuilders().getEntityVertexConsumers();
		OutlineMultiBufferSource outlineProvider = Minecraft.getInstance().getBufferBuilders().getOutlineVertexConsumers();

		for (TotemDollRenderRequest request : this.requests) {
			this.renderRequest(request, request.provider() == null ? mainProvider : request.provider(), outlineProvider);
		}

		this.requests.clear();
		mainProvider.drawCurrentLayer();
		// We should draw this before unlocking, to make sure that atlas won't be changed earlier than the draw call
		atlasTexture.setLocked(false);
	}

	private void renderRequest(TotemDollRenderRequest request, MultiBufferSource mainProvider, @SuppressWarnings("unused") OutlineMultiBufferSource outlineProvider) {
		this.matrices.push();
		this.matrices.peek().copyFrom(request.copyPeek());

		TotemDollData data = request.data();
		this.tempProperties.copyFrom(data.getRenderProperties());

		data.getRenderProperties().copyFrom(request.renderProperties());
		data.clearFrameModel();
		TotemDollModel modelToRender = data.getModelToRender();
		if (modelToRender == null) {
			this.matrices.pop();
			return;
		}
		modelToRender.resetPartsVisibility();
		data.getRenderProperties().applyToModel(modelToRender);

		TotemDollRenderer.renderDoll(this.matrices, data, request.holdingPlayer(), request.context(), mainProvider, request.light(), request.overlay());
		//? if >=1.21.9 {
		int argb = request.outlineColor();
		if (argb != 0) {
			outlineProvider.setColor(argb);
			TotemDollRenderer.renderDoll(this.matrices, data, request.holdingPlayer(), request.context(), outlineProvider, request.light(), request.overlay());
		}//?}

		data.getRenderProperties().copyFrom(this.tempProperties);

		this.matrices.pop();
	}

}
