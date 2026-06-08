package com.darkz.skintotem.yacl.custom.category.better;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.gui.*;
import dev.isxander.yacl3.gui.YACLScreen.CategoryTab;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.extension.DrawContextExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.network.chat.Component;
import net.minecraft.util.math.RotationAxis;

import com.mojang.blaze3d.systems.RenderSystem;

import com.darkz.skintotem.utils.*;
import com.darkz.skintotem.yacl.custom.TransparencySprites;
import com.darkz.skintotem.yacl.custom.screen.SkinTotemModYACLScreen;

@ExtensionMethod(DrawContextExtension.class)
public class BetterCategoryTab extends CategoryTab {

	private final ScreenRect rightPaneDim;

	public BetterCategoryTab(YACLScreen screen, ConfigCategory category, ScreenRect tabArea) {
		super(screen, category, tabArea);
		this.rightPaneDim = new ScreenRect(screen.width / 3 * 2, tabArea.getTop() + 1, screen.width / 3, tabArea.height());
	}

	@Override
	public void renderBackground(GuiGraphics context) {
		RenderUtils.enableBlend();
		RenderUtils.enableDepthTest();

		// right pane darker db
		DrawUtils.drawTexture(context, TransparencySprites.getMenuListBackgroundTexture(), rightPaneDim.getLeft(), rightPaneDim.getTop(), rightPaneDim.getRight() + 2, rightPaneDim.getBottom() + 2, rightPaneDim.width() + 2, rightPaneDim.height() + 2, 32, 32);

		// top separator for right pane
		context.push();
		context.translate(0, 0, 10);
		DrawUtils.drawTexture(context, TransparencySprites.getMenuSeparatorTexture(), rightPaneDim.getLeft() - 1, rightPaneDim.getTop() - 2, 0.0F, 0.0F, rightPaneDim.width() + 1, 2, 32, 2);
		context.pop();

		// left separator for right pane
		context.push();
		context.translate(rightPaneDim.getLeft(), rightPaneDim.getTop() - 1, 0);
		context.rotateZ(90);
		DrawUtils.drawTexture(context, TransparencySprites.getMenuSeparatorTexture(), 0, 0, 0f, 0f, rightPaneDim.height() + 1, 2, 32, 2);
		context.pop();

		RenderUtils.disableBlend();
		RenderUtils.disableDepthTest();
	}

	@Override
	public void updateButtons() {
		this.undoButton.active = false;
		this.saveFinishedButton.setMessage(ScreenTexts.DONE);
		this.saveFinishedButton.setTooltip(Tooltip.of(Component.translatable("yacl.gui.finished.tooltip")));
		this.cancelResetButton.setMessage(Component.translatable("controls.reset"));
		this.cancelResetButton.setTooltip(Tooltip.of(Component.translatable("yacl.gui.reset.tooltip")));
	}
}
