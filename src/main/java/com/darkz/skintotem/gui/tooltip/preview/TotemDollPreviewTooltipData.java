package com.darkz.skintotem.gui.tooltip.preview;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.doll.data.TotemDollData;

public record TotemDollPreviewTooltipData(TotemDollData data, ResourceLocation model) implements TooltipComponent {

}
