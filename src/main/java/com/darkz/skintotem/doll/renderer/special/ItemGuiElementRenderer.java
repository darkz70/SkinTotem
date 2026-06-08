package com.darkz.skintotem.doll.renderer.special;

//? if >=1.21.6 {
import com.darkz.skintotem.SkinTotemMod;
import net.minecraft.client.Minecraft;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.DiffuseLighting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

//? if >=1.21.9 {



//?}

public class ItemGuiElementRenderer extends SpecialGuiElementRenderer<ItemGuiRenderState> {

	private final ItemStackRenderState itemRenderState = new ItemStackRenderState();

	public ItemGuiElementRenderer(Immediate vertexConsumers) {
		super(vertexConsumers);
	}

	@Override
	public Class<ItemGuiRenderState> getElementClass() {
		return ItemGuiRenderState.class;
	}

	@Override
	protected void render(ItemGuiRenderState state, PoseStack matrices) {
		Minecraft client = Minecraft.getInstance();

		client.gameRenderer.getDiffuseLighting().setShaderLights(Type.ITEMS_FLAT);
		matrices.multiply(state.rotation());
		float size = state.size();
		matrices.scale(-size, -size, size);
		this.renderItem(
				state.stack(),
				ItemDisplayContext.FIXED,
				15728880,
				OverlayTexture.DEFAULT_UV,
				matrices,
				this.vertexConsumers,
				client.world,
				0
		);
	}

	@Override
	protected float getYOffset(int height, int windowScaleFactor) {
		return height / 2F;
	}

	@Override
	protected String getName() {
		return "%s-item-special-gui-renderer".formatted(SkinTotemMod.MOD_ID);
	}

	public void renderItem(ItemStack stack, ItemDisplayContext displayContext, int light, int overlay, PoseStack matrices, MultiBufferSource vertexConsumers, @Nullable World world, int seed) {
		this.renderItem(null, stack, displayContext, matrices, vertexConsumers, world, light, overlay, seed);
	}

	public void renderItem(@Nullable LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, PoseStack matrices, MultiBufferSource vertexConsumers, @Nullable World world, int light, int overlay, int seed) {
		Minecraft.getInstance().getItemModelManager().clearAndUpdate(this.itemRenderState, stack, displayContext, world, entity, seed);
		//? if >=1.21.9 {
		RenderDispatcher dispatcher = Minecraft.getInstance().gameRenderer.getEntityRenderDispatcher();
		this.itemRenderState.render(matrices, dispatcher.getQueue(), light, overlay, 0);
		dispatcher.render();
		//?} else {
		/*this.itemRenderState.render(matrices, vertexConsumers, light, overlay);
		*///?}
	}
}
//?}
