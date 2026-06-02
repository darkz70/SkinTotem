package com.darkz.skintotem.gui.tooltip.state;

//? if >=1.21 {
import net.minecraft.item.tooltip.TooltipData;
 //?} else {
/*import net.minecraft.client.item.TooltipData;
*///?}

import com.darkz.skintotem.doll.data.LoadingState;

public record LoadingStateTooltipData(LoadingState state) implements TooltipData {

}
