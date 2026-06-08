package com.darkz.skintotem.gui.tooltip.wrapped;

//? if >=1.21 {
import net.minecraft.world.item.tooltip.TooltipProvider;
 //?} else {
/*import net.minecraft.client.item.TooltipProvider;
*///?}
import net.minecraft.network.chat.Component;

public record WrappedTextTooltipProvider(Component text) implements TooltipProvider {

}
