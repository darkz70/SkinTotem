package com.darkz.skintotem.gui.tooltip.preview;

import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.utils.DrawUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.doll.data.SkinTotemData;
import com.darkz.skintotem.extension.ResourceLocationExtension;

@ExtensionMethod(ResourceLocationExtension.class)
public class SkinTotemPreviewTooltipComponent implements ClientTooltipComponent {

	private final SkinTotemData data;
	private final ResourceLocation modelId;

	public SkinTotemPreviewTooltipComponent(SkinTotemData data, ResourceLocation modelId) {
		this.data    = data;
		this.modelId = modelId;
		this.data.setStandardMModel(modelId);
	}

	@Override
	public int getHeight() {
		return SkinTotemConfig.getInstance().getBetterTagMenuTooltipSize() + 10;
	}

	@Override
	public int getWidth(Font textRenderer) {
		return SkinTotemConfig.getInstance().getBetterTagMenuTooltipSize();
	}

	@Override
	public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
		int width = this.getWidth(textRenderer);
		SkinTotemConfig config = SkinTotemConfig.getInstance();
		float sizeOriginal = config.getBetterTagMenuTooltipSize();
		float size = (sizeOriginal / 1.25F) * config.getTagMenuTooltipModelScale();
		Component text = Component.literal(this.modelId.getFileName());
		int textWidth = textRenderer.width(text);

		int height = this.getHeight();
		context.enableScissor(x, y + 10 + 4 + 2, x + width, y + height - 2);

		SkinTotemRenderer.renderPreview(context, x, y + 10, width, height - 10, size, this.data, DollRenderContext.D_TOOLTIP);

		context.disableScissor();

		context.enableScissor(x, y, x + width, y + height);
		if (textWidth > width) {
			DrawUtils.drawText(context, text, x, y, width, 10);
		} else {
			context.drawString(textRenderer, text, x, y + 1, -1, true);
		}
		context.fill(x, y + 10 + 3, x + Math.min((textWidth - 5), width), y + 10 + 4, -1);
		context.disableScissor();
	}
}
