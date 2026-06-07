package com.darkz.skintotem.gui.widget.info;

import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.gui.tooltip.info.InfoTooltipData;

public class TipsWidget extends InfoWidget {

	public static final Identifier TEXTURE = SkinTotemMod.id("textures/gui/info/tips.png");

	public TipsWidget(int x, int y) {
		super(x, y, 9, 9, new InfoTooltipData("tags.tips", -1), TEXTURE);
	}
}
