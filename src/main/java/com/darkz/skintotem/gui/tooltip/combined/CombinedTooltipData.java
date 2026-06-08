package com.darkz.skintotem.gui.tooltip.combined;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
//? if >=1.21 {
import net.minecraft.world.item.tooltip.TooltipProvider;
//?} else {
/*import net.minecraft.client.item.TooltipProvider;
*///?}

import java.util.*;

public record CombinedTooltipProvider(List<TooltipComponent> list) implements TooltipProvider {

	public CombinedTooltipProvider(TooltipProvider... data) {
		this(Arrays.stream(data).map(TooltipComponent::of).toList());
	}

}
