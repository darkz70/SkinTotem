package com.darkz.skintotem.gui.tooltip.wrapped;

//? if >=1.21 {
import net.minecraft.item.tooltip.TooltipData;
 //?} else {
/*import net.minecraft.client.item.TooltipData;
*///?}
import net.minecraft.text.Text;

public record WrappedTextTooltipData(Text text) implements TooltipData {

}
