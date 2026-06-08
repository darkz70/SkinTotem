package com.darkz.skintotem.gui.tooltip.info;

//? if >=1.21 {
import net.minecraft.world.item.tooltip.TooltipProvider;
 //?} else {
/*import net.minecraft.client.item.TooltipProvider;
*///?}

public record InfoTooltipProvider(String key, int color) implements TooltipProvider {

}
