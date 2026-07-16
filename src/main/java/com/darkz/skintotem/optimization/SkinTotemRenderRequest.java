package com.darkz.skintotem.optimization;

import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.doll.renderer.DollRenderContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack.Entry;
import org.jetbrains.annotations.Nullable;

public record SkinTotemRenderRequest(
		Entry copyPeek,
		SkinTotemData data,
		SkinTotemRenderProperties renderProperties,
		AbstractClientPlayerEntity holdingPlayer,
		DollRenderContext context,
		int light,
		int overlay,
		int outlineColor,
		@Nullable VertexConsumerProvider provider) {

}
