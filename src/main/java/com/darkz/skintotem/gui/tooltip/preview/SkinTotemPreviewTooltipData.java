package com.darkz.skintotem.gui.tooltip.preview;

//? if >=1.21 {
import net.minecraft.item.tooltip.TooltipData;
 //?} else {
/*import net.minecraft.client.item.TooltipData;
*///?}

import net.minecraft.util.Identifier;

import com.darkz.skintotem.doll.data.SkinTotemData;

public record SkinTotemPreviewTooltipData(SkinTotemData data, Identifier model) implements TooltipData {

}
