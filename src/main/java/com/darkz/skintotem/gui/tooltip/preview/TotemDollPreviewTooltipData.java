package com.darkz.skintotem.gui.tooltip.preview;

//? if >=1.21 {
import net.minecraft.world.item.tooltip.TooltipProvider;
 //?} else {
/*import net.minecraft.client.item.TooltipProvider;
*///?}

import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.doll.data.TotemDollData;

public record TotemDollPreviewTooltipProvider(TotemDollData data, Identifier model) implements TooltipProvider {

}
