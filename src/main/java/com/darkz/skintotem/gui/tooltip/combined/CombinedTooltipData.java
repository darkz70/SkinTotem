package com.darkz.skintotem.gui.tooltip.combined;

import net.minecraft.world.item.tooltip.TooltipData;
import java.util.List;
import java.util.Arrays;

public record CombinedTooltipData(List<TooltipData> list) implements TooltipData {

	public CombinedTooltipData(TooltipData... data) {
		this(Arrays.asList(data));
	}

}
