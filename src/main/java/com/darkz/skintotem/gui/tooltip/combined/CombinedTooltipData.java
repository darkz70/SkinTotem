package com.darkz.skintotem.gui.tooltip.combined;

import net.minecraft.client.gui.tooltip.TooltipComponent;
//? if >=1.21 {
import net.minecraft.item.tooltip.TooltipData;
//?} else {
/*import net.minecraft.client.item.TooltipData;
*///?}

import java.util.*;

public record CombinedTooltipData(List<TooltipComponent> list) implements TooltipData {

	public CombinedTooltipData(TooltipData... data) {
		this(Arrays.stream(data).map(TooltipComponent::of).toList());
	}

}
