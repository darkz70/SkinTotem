package com.darkz.skintotem.gui.tooltip.info;

//? if >=1.21 {
import net.minecraft.world.item.tooltip.TooltipProvider;
 //?} else {
/*import net.minecraft.client.item.TooltipProvider;
*///?}

public record InfoTooltipData(String key, int color) implements net.minecraft.world.item.tooltip.TooltipData {

}
