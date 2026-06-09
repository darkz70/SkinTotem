package com.darkz.skintotem.gui.widget.tag;

import lombok.*;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.model.base.MModel;
import com.darkz.skintotem.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.config.SkinTotemModConfig;
import com.darkz.skintotem.doll.manager.StandardTotemDollManager;
import com.darkz.skintotem.doll.renderer.TotemDollRenderer;
import com.darkz.skintotem.gui.tooltip.preview.TotemDollPreviewTooltipData;
import com.darkz.skintotem.tag.*;
import com.darkz.skintotem.tag.manager.TagsManager;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
public class CustomModelTagButtonWidget extends TagButtonWidget {

	@Nullable
	private final ResourceLocation model;
	private TotemDollData data;
	@Nullable
	private TotemDollData tooltipData;
	private boolean tooltipDataActive = false;

	public CustomModelTagButtonWidget(Tag tag, int x, int y, TagPressAction pressAction) {
		super(tag, x, y, pressAction);
		this.model = Optional.ofNullable(TagsManager.getCustomModelIdsTags().get(tag.getTag())).map(CustomModelTag::getModelId).orElse(null);
		this.data = StandardTotemDollManager.getStandardDoll().copy();
	}

	public void updateData(TotemDollData data) {
		if (data == null) {
			return;
		}
		this.data.getRenderProperties().copyFrom(data.getRenderProperties());
	}

	@Override
	public void /*? if >=1.21 {*/renderWidget/*?} else {*//*renderButton*//*?}*/(GuiGraphics context, int mouseX, int mouseY, float delta) {
		if (this.model != null) {
			this.data.setFrameMModel(this.model);
		}
		super./*? if >=1.21 {*/renderWidget/*?} else {*//*renderButton*//*?}*/(context, mouseX, mouseY, delta);
		if (!this.tooltipDataActive) {
			this.tooltipData = null;
		}
		this.tooltipDataActive = false;
	}

	@Override
	protected void renderIcon(GuiGraphics context, int x, int y) {
		context.enableScissor(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1);
		TotemDollRenderer.renderPreview(context, x, y, this.getWidth(), this.getHeight(),  Math.min(this.getWidth(), this.getHeight()), this.getData());
		context.disableScissor();
	}

	@Override
	public @Nullable net.minecraft.client.gui.screens.inventory.tooltip.TooltipComponent getTooltipComponent() {
		if (this.model == null) {
			return ClientTooltipComponent.create(Component.literal("Unknown Model").getVisualOrderText());
		}
		if (this.tooltipData == null) {
			this.tooltipData = this.data.copy();
			this.tooltipData.setStandardMModel(this.data.getRenderProperties().getStandardMModel());
		}
		this.tooltipDataActive = true;
		return ClientTooltipComponent.create(new TotemDollPreviewTooltipData(this.tooltipData, this.model));
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, /*? if >=1.21 {*/ double horizontalAmount, /*?}*/ double verticalAmount) {
		if (!this.isMouseOver(mouseX, mouseY)) {
			return false;
		}
		int amount = ((int) verticalAmount) > 0 ? 1 : -1;
		SkinTotemModConfig config = SkinTotemModConfig.getInstance();
		if (ScreenUtils.hasShiftDown()) {
			config.setBetterTagMenuTooltipSize(Mth.clamp(config.getBetterTagMenuTooltipSize() + (amount * 2), 60, 500));
			return true;
		} else if (ScreenUtils.hasControlDown()) {
			config.setTagMenuTooltipModelScale(Mth.clamp(config.getTagMenuTooltipModelScale() + (amount / 12F), 0.1F, 10F));
			return true;
		}
		return false;
	}
}
