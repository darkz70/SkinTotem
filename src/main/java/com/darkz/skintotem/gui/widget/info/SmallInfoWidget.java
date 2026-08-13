package com.darkz.skintotem.gui.widget.info;

import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.gui.tooltip.info.InfoTooltipData;
import com.darkz.skintotem.utils.ColorUtils;

public class SmallInfoWidget extends InfoWidget {

	public static final ResourceLocation TEXTURE = SkinTotem.id("textures/gui/info/info_small.png");
	public static final int TITLE_COLOR = ColorUtils.getRgb(89, 206, 255);

	public SmallInfoWidget(int x, int y) {
		super(x, y, 9, 10, new InfoTooltipData("tags.info", TITLE_COLOR), TEXTURE);
	}
}
