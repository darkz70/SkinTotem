package com.darkz.skintotem.gui.tooltip.wrapped;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.tooltip.TooltipData;

public record WrappedTextTooltipData(Component text) implements TooltipData {

}
