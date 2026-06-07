package com.darkz.skintotem.gui.tooltip.preview;

//? if >=1.21 {
import net.minecraft.item.tooltip.TooltipData;
 //?} else {
/*import net.minecraft.client.item.TooltipData;
*///?}

import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.doll.data.TotemDollData;

public record TotemDollPreviewTooltipData(TotemDollData data, Identifier model) implements TooltipData {

}
