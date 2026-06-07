package com.darkz.skintotem.doll.renderer;

import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.atlas.AtlasSprite;
import com.darkz.skintotem.extension.*;
import com.darkz.skintotem.optimization.TotemDollRenderRequestsCollector;
import com.darkz.skintotem.thing.ThingMarks;
import com.darkz.skintotem.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.item.*;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.config.SkinTotemModConfig;
import com.darkz.skintotem.config.rendering.*;
import com.darkz.skintotem.config.totem.TotemDollSkinType;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.doll.manager.StandardTotemDollManager;
import com.darkz.skintotem.doll.model.TotemDollModel;
import com.darkz.skintotem.doll.model.TotemDollModel.Drawer;
import com.darkz.skintotem.utils.plugin.TotemDollPlugin;

import net.minecraft.network.chat.Component;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

//? if >=1.21 {

import net.minecraft.core.component.DataComponents;

//?}

@ExtensionMethod({ItemStackExtension.class, DrawContextExtension.class})
public class TotemDollRenderer {

	public static boolean sentRenderRequest(MatrixStack matrices, ItemStack stack, DollRenderContext context, int light, int overlay, int outlineColor, @Nullable VertexConsumerProvider provider) {
		if (canRender(stack)) {
			TotemDollData totemDollData = stack.getTotemDollData(false);
			TotemDollRenderRequestsCollector.getInstance().requestRender(matrices, totemDollData, stack.getPlayerEntity(), context, light, overlay, outlineColor, provider);
			if (!ThingMarks.WORLD_RENDERING.get().isMarked()) {
				TotemDollRenderRequestsCollector.getInstance().render();
			}
			return true;
		}
		return false;
	}

	public static void renderDoll(MatrixStack matrices, ItemStack stack, DollRenderContext context, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		renderDoll(matrices, stack.getTotemDollData(), stack.getPlayerEntity(), context, vertexConsumers, light, overlay);
	}

	public static void renderDoll(MatrixStack matrices, TotemDollData totemDollData, AbstractClientPlayerEntity holdingPlayer, DollRenderContext context, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		DollRenderContext renderContext = context == DollRenderContext.D_NONE ? DollRenderContext.D_GUI : context;
		beforeDollRendered(renderContext, holdingPlayer, totemDollData);
		matrices.push();

		renderContext.apply(totemDollData.getModelToRender().getMain(), matrices);
		totemDollData.getRenderProperties().setRenderContext(renderContext);
		matrices.translate(-0.5F, -1.0F, -0.5F);

		switch (renderContext) {
			case D_FIRST_PERSON_LEFT_HAND,
			     D_FIRST_PERSON_RIGHT_HAND -> TotemDollRenderer.renderInHand(renderContext.isLeftHanded(), true, matrices, vertexConsumers, light, overlay, totemDollData);
			case D_THIRD_PERSON_LEFT_HAND,
			     D_THIRD_PERSON_RIGHT_HAND -> TotemDollRenderer.renderInHand(renderContext.isLeftHanded(), false, matrices, vertexConsumers, light, overlay, totemDollData);
			default -> TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
		}

		afterDollRenderer();
		matrices.pop();
	}

	public static void renderPreview(DrawContext context, int x, int y, int width, int height, float size, @Nullable TotemDollData data) {
		renderPreview(context, x, y, width, height, size, data, DollRenderContext.D_PREVIEW);
	}

	public static void renderPreview(DrawContext context, int x, int y, int width, int height, float size, @Nullable TotemDollData data, DollRenderContext renderContext) {
		//? if >=1.21.6 {
		if (data == null) {
			long currentTime = Util.getMeasuringTimeMs();
			float rotationSpeed = 0.05f;
			float rotation = (currentTime * rotationSpeed) % 360;
			context.state.addSpecialElement(new com.darkz.skintotem.doll.renderer.special.ItemGuiRenderState(Items.TOTEM_OF_UNDYING.getDefaultStack(), x, y, width, height, size, RotationAxis.POSITIVE_Y.rotationDegrees(rotation), context.scissorStack.peekLast()));
		} else {
			data.getRenderProperties().setRenderContext(renderContext);
			context.state.addSpecialElement(com.darkz.skintotem.doll.renderer.special.TotemDollRenderState.getPreview(data, x, y, width, height, size, context.scissorStack.peekLast()));
		}
		//?} else {
		/*if (data == null) {
			renderVanillaTotemPreview(context, x, y, width, height, size);
		} else {
			data.getRenderProperties().setRenderContext(renderContext);
			context.getMatrices().push();
			int centerX = x + (width / 2);
			int centerY = y + (height / 2);
			context.getMatrices().translate(centerX, centerY, 300F);
			context.getMatrices().scale(-1.0F, 1.0F, 1.0F);
			renderDataPreview(context.getMatrices(), context.vertexConsumers, context::draw, size, data);
			context.getMatrices().pop();
		}*///?}
	}

	public static void renderDataPreview(MatrixStack matrices, Immediate consumers, Runnable draw, float size, @NotNull TotemDollData data) {
		float i = (size / 2F);

		long currentTime = Util.getMeasuringTimeMs();
		float rotationSpeed = 0.05f;

		float rotation = (currentTime * rotationSpeed) % 360;

		LightningUtils.disable3dLighting();
		matrices.push();
		matrices.scale(-i, -i, i);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
		matrices.translate(-0.5F, -1.0F, -0.5F);
		TotemDollRenderer.render(matrices, consumers, 15728880, OverlayTexture.DEFAULT_UV, data);
		matrices.pop();
		draw.run();
		LightningUtils.enable3dLighting();
	}

	//? if <=1.21.5 {
	/*public static void renderVanillaTotemPreview(DrawContext context, int x, int y, int width, int height, float size) {
		float i = (size / 2F);
		int centerX = x + (width / 2);
		int centerY = y + (height / 2);
		long currentTime = Util.getMeasuringTimeMs();
		float rotationSpeed = 0.05f;

		float rotation = (currentTime * rotationSpeed) % 360;

		float v = i / 16;
		float d = i / 2;

		context.push();
		context.translate(centerX - d, centerY - d, 400F);
		context.translate(d, d, 0F);
		context.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
		context.translate(-d, -d, 0F);
		context.scale(v, v, v);
		context.translate(0F, 0F, -150F); // I hate this
		context.drawItemWithoutEntity(Items.TOTEM_OF_UNDYING.getDefaultStack(), 0, 0);
		context.pop();
	}
	*///?}

	public static void renderInHand(boolean leftHanded, boolean firstPerson, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollData totemDollData) {
		matrices.push();

		if (firstPerson) {
			SkinTotemModConfig config = SkinTotemModConfig.getInstance();
			RenderingConfig renderingConfig = config.getRenderingConfig();
			HandRenderingConfig handRenderingConfig = leftHanded ? renderingConfig.getLeftHandConfig() : renderingConfig.getRightHandConfig();

			matrices.translate((handRenderingConfig.getOffsetZ() / 100F) * (leftHanded ? 1 : -1), handRenderingConfig.getOffsetY() / 100F, handRenderingConfig.getOffsetX() / 100F);

			matrices.translate(0.5F, 0.5F, 0.5F);

			double scale = handRenderingConfig.getScale();
			matrices.scale((float) scale, (float) scale, (float) scale);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) handRenderingConfig.getRotationX()));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) handRenderingConfig.getRotationY() * (leftHanded ? -1 : 1)));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) handRenderingConfig.getRotationZ() * (leftHanded ? -1 : 1)));

			matrices.translate(-0.5F, -0.5F, -0.5F);
		}

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
		matrices.pop();
	}

	public static void render(MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay, TotemDollData totemDollData) {
		TotemDollSprites textures = totemDollData.getSpritesToRender();
		AtlasSprite skinSprite = textures.getSkinSprite();
		AtlasSprite capeSprite = textures.getCapeSprite();
		AtlasSprite elytraSprite = textures.getElytraSprite();
		TotemDollModel model = totemDollData.getModelToRender();
		if (model == null) return;

		String nickname = totemDollData.getNickname();

		if (nickname != null && (nickname.equalsIgnoreCase("dinnerbone") || nickname.equalsIgnoreCase("grumm"))) {
			matrices.translate(0.5F, 1.0F, 0.5F);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
			matrices.translate(-0.5F, -1.0F, -0.5F);
		}

		matrices.push();
		matrices.translate(0.5F, 0.5F, 0.5F);
		matrices.scale(-1.0F, -1.0F, 1.0F); // - - 0
		matrices.translate(-0.5F, -0.5F, -0.5F);

		Drawer drawer = model.getDrawer();

		if (nickname != null && nickname.equals("deadmau5")) {
			drawer.requestDrawingPartWithSprite("ears", skinSprite);
		}

		if (capeSprite != null && capeSprite.isUploaded()) {
			drawer.requestDrawingPartWithSprite("cape", capeSprite);
		}

		if (elytraSprite.isUploaded()) {
			drawer.requestDrawingPartWithSprite("elytra", elytraSprite);
		}

		drawer.draw(matrices, provider, skinSprite, light, overlay, /*? if >=1.21 {*/ -1 /*?} else {*/ /*1.0F, 1.0F, 1.0F, 1.0F *//*?}*/);

		matrices.pop();
	}

	private static void beforeDollRendered(@Nullable DollRenderContext context, AbstractClientPlayerEntity playerEntity, TotemDollData totemDollData) {
		Profiler profiler = ProfilerUtils.getProfiler();
		profiler.swap(SkinTotemMod.MOD_ID);

		if (context == DollRenderContext.D_GUI && SkinTotemModConfig.getInstance().getStandardTotemDollSkinType() == TotemDollSkinType.HOLDING_PLAYER) {
			playerEntity = Minecraft.getInstance().player;
		}

		if (StandardTotemDollManager.getStandardDoll().equals(totemDollData)) {
			TotemDollRenderer.prepareStandardDollForRendering(playerEntity, totemDollData);
		}
	}

	private static void prepareStandardDollForRendering(AbstractClientPlayerEntity playerEntity, TotemDollData totemDollData) {
		if (playerEntity != null && SkinTotemModConfig.getInstance().getStandardTotemDollSkinType() == TotemDollSkinType.HOLDING_PLAYER) {
			if (!playerEntity.equals(Minecraft.getInstance().player) && playerEntity.isInvisibleTo(Minecraft.getInstance().player)) {
				return;
			}
			totemDollData.setFrameSprites(playerEntity);
		}
	}

	private static void afterDollRenderer() {
		Profiler profiler = ProfilerUtils.getProfiler();
		profiler.pop();
	}

	public static boolean canRender(@Nullable ItemStack stack) {
		if (!SkinTotemModClient.canProcess(stack)) {
			return false;
		}
		if (stack.hasModdedModel()) {
			return false;
		}
		Text realCustomName = stack.getRealCustomName();
		boolean standardDollWithoutName = realCustomName == null;
		if (standardDollWithoutName && SkinTotemModConfig.getInstance().isUseVanillaTotemModel()) {
			return false;
		}
		return !TotemDollPlugin.work(realCustomName);
	}
}
