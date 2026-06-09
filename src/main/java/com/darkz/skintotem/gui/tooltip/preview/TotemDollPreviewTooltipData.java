package com.darkz.skintotem.gui.tooltip.preview;

import com.darkz.skintotem.doll.data.TotemDollData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.tooltip.TooltipData;

public record TotemDollPreviewTooltipData(TotemDollData data, ResourceLocation model) implements TooltipData {

}
