package com.darkz.skintotem.gui.tooltip.preview;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.doll.data.SkinTotemData;

public record SkinTotemPreviewTooltipData(SkinTotemData data, ResourceLocation model) implements TooltipComponent {

}
