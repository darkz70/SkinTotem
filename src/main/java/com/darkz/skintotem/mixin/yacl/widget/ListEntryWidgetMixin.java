package com.darkz.skintotem.mixin.yacl.widget;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.ListOptionEntry;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.*;
import dev.isxander.yacl3.gui.controllers.*;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import com.darkz.skintotem.config.other.simple.SimpleEntry;

import java.util.List;

@Pseudo
@Mixin(ListEntryWidget.class)
public class ListEntryWidgetMixin {

	@Dynamic
	@Shadow(remap = false)
	@Final
	private ListOptionEntry<?> listOptionEntry;

	@Dynamic
	@Shadow(remap = false)
	@Final
	private TooltipButtonWidget moveDownButton;

	@Dynamic
	@Shadow(remap = false)
	@Final
	private TooltipButtonWidget moveUpButton;

	@Dynamic
	@Shadow(remap = false)
	@Final
	private AbstractWidget entryWidget;

	@Dynamic
	@Shadow(remap = false)
	@Final
	private TooltipButtonWidget removeButton;

	@Dynamic
	@WrapOperation(at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/gui/AbstractWidget;setDimension(Ldev/isxander/yacl3/api/utils/Dimension;)V"), method = "<init>")
	private void wrapDimension(AbstractWidget instance, Dimension<Integer> dim, Operation<Void> original, @Local(argsOnly = true) ListOptionEntry<?> listOptionEntry) {
		if (!(listOptionEntry.pendingValue() instanceof SimpleEntry<?, ?>)) {
			return;
		}
		instance.setDimension(instance.getDimension().clone().expand(-20, 0));
	}

	@Dynamic
	@WrapWithCondition(at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/gui/TooltipButtonWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"), method = "render")
	private boolean disableRendering1(TooltipButtonWidget instance, DrawContext context, int x, int y, float v) {
		return !((instance == this.moveDownButton || instance == this.moveUpButton) && (this.listOptionEntry.pendingValue() instanceof SimpleEntry<?, ?>));
	}

	@Dynamic
	@WrapWithCondition(at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/gui/TooltipButtonWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"), method = "render")
	private boolean disableRendering2(TooltipButtonWidget instance, DrawContext context, int x, int y, float v) {
		return !((instance == this.moveDownButton || instance == this.moveUpButton) && (this.listOptionEntry.pendingValue() instanceof SimpleEntry<?, ?>));
	}

	@Dynamic
	@Inject(at = @At("RETURN"), method = "children", cancellable = true)
	private void wrapChildren(CallbackInfoReturnable<List<? extends Element>> cir) {
		if (!(this.listOptionEntry.pendingValue() instanceof SimpleEntry<?, ?>)) {
			return;
		}
		cir.setReturnValue(ImmutableList.of(this.entryWidget, this.removeButton));
	}
}
