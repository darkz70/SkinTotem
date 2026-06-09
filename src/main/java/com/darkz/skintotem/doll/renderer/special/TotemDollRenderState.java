package com.darkz.skintotem.doll.renderer.special;

//? if >=1.21.6 {

import com.darkz.skintotem.doll.data.TotemDollData;
import com.darkz.skintotem.doll.renderer.DollRenderContext;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

public record TotemDollRenderState(
		@Nullable
		TotemDollData data,
		@Nullable
		ItemStack stack,
		int x,
		int y,
		int width,
		int height,
		float size,
		DollRenderContext renderContext,
		Matrix3x2f matrices,
		@Nullable ScreenRectangle scissorArea,
		@Nullable ScreenRectangle bounds
) implements SpecialGuiElementRenderState {

	public static TotemDollRenderState getGui(ItemStack stack, int x, int y, Matrix3x2f matrices, @Nullable ScreenRectangle scissorArea) {
		return new TotemDollRenderState(null, stack, x, y, 16, 16, 16, DollRenderContext.D_GUI, matrices, scissorArea, SpecialGuiElementRenderState.createBounds(x, y, x + 16, y + 16, scissorArea));
	}

	public static TotemDollRenderState getPreview(TotemDollData data, int x, int y, int width, int height, float size, @Nullable ScreenRectangle scissorArea) {
		return new TotemDollRenderState(data, null, x, y, width, height, size, DollRenderContext.D_PREVIEW, null, scissorArea, SpecialGuiElementRenderState.createBounds(x, y, x + width, y + height, scissorArea));
	}

	@Override
	public int x1() {
		return this.x();
	}

	@Override
	public int y1() {
		return this.y();
	}

	@Override
	public int x2() {
		return this.x1() + this.width();
	}

	@Override
	public int y2() {
		return this.y1() + this.height();
	}

	@Override
	public float scale() {
		return 1.0F;
	}

	@Override
	public Matrix3x2f pose() {
		if (this.matrices == null) {
			return SpecialGuiElementRenderState.super.pose();
		}
		return this.matrices;
	}
}

//?}