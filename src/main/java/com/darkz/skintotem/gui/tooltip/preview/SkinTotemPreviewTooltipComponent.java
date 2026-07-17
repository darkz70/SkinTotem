package com.darkz.skintotem.gui.tooltip.preview;

import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.doll.renderer.*;
import com.darkz.skintotem.utils.DrawUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.doll.data.SkinTotemData;
import com.darkz.skintotem.extension.IdExtension;

@ExtensionMethod(IdExtension.class)
public class SkinTotemPreviewTooltipComponent implements TooltipComponent {

	private final SkinTotemData data;
	private final Identifier modelId;

	public SkinTotemPreviewTooltipComponent(SkinTotemData data, Identifier modelId) {
		this.data    = data;
		this.modelId = modelId;
		this.data.setStandardMModel(modelId);
	}

	@Override
	public int getHeight(/*? >=1.21.2 {*/TextRenderer textRenderer/*?}*/) {
		return SkinTotemConfig.getInstance().getBetterTagMenuTooltipSize() + 10;
	}

	@Override
	public int getWidth(TextRenderer textRenderer) {
		return SkinTotemConfig.getInstance().getBetterTagMenuTooltipSize();
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y,/*? >=1.21.2 {*/int w, int h,/*?}*/ DrawContext context) {
		int width = this.getWidth(textRenderer);
		SkinTotemConfig config = SkinTotemConfig.getInstance();
		float sizeOriginal = config.getBetterTagMenuTooltipSize();
		float size = (sizeOriginal / 1.25F) * config.getTagMenuTooltipModelScale();
		Text text = Text.of(this.modelId.getFileName());
		int textWidth = textRenderer.getWidth(text);

		int height = this.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/);
		context.enableScissor(x, y + 10 + 4 + 2, x + width, y + height - 2);

		SkinTotemRenderer.renderPreview(context, x, y + 10, width, height - 10, size, this.data, DollRenderContext.D_TOOLTIP);

		context.disableScissor();

		context.enableScissor(x, y, x + width, y + height);
		if (textWidth > width) {
			DrawUtils.drawText(context, text, x, y, width, 10);
		} else {
			context.drawText(textRenderer, text, x, y + 1, -1, true);
		}
		context.fill(x, y + 10 + 3, x + Math.min((textWidth - 5), width), y + 10 + 4, -1);
		context.disableScissor();
	}
}
