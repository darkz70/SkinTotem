package com.darkz.skintotem.gui.widget.info;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.*;
//? if >=1.21 {
import net.minecraft.item.tooltip.TooltipData;
 //?} else {
/*import net.minecraft.client.item.TooltipData;
*///?}
import net.minecraft.util.Identifier;

import com.darkz.skintotem.utils.DrawUtils;
import com.darkz.skintotem.utils.tooltip.IRequestableTooltipScreen;

import java.util.List;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
public class InfoWidget implements Drawable {

	public boolean visible = true;
	private int x;
	private int y;
	private int width;
	private int height;

	private boolean hovered;

	private Identifier texture;

	@Nullable
	private TooltipData tooltipData;

	public InfoWidget(int x, int y, int width, int height, @Nullable TooltipData tooltipData, Identifier texture) {
		this.x           = x;
		this.y           = y;
		this.width       = width;
		this.height      = height;
		this.texture     = texture;
		this.tooltipData = tooltipData;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (!this.isVisible()) {
			return;
		}

		this.hovered = /*? if >=1.21 {*/context.scissorContains(mouseX, mouseY) &&  /*?}*/ mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

		DrawUtils.drawTexture(context, this.texture, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

		this.requestTooltip();
	}

	public void requestTooltip() {
		MinecraftClient client = MinecraftClient.getInstance();
		Screen screen = client.currentScreen;
		TextRenderer textRenderer = client.textRenderer;

		if (!(screen instanceof IRequestableTooltipScreen tooltipScreen)) {
			return;
		}

		if (!this.isHovered()) {
			return;
		}

		if (this.tooltipData == null) {
			return;
		}

		TooltipComponent component = TooltipComponent.of(this.tooltipData);
		tooltipScreen.myTotemDoll$requestTooltip(((c, x, y, d) -> {
			DrawUtils.drawTooltip(c, List.of(component), x, y);
		}));
	}

	public void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
}
