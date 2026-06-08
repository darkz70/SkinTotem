package com.darkz.skintotem.gui.tooltip.state;

//? if >=1.21 {
import net.minecraft.world.item.tooltip.TooltipProvider;
 //?} else {
/*import net.minecraft.client.item.TooltipProvider;
*///?}

import com.darkz.skintotem.doll.data.LoadingState;

public record LoadingStateTooltipData(LoadingState state) implements net.minecraft.world.item.tooltip.TooltipData {

}
