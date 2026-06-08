package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.config.SkinTotemModConfig;
import com.darkz.skintotem.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.*;
//? if >=1.21 {
import net.minecraft.world.item.tooltip.TooltipProvider;
 //?} else {
/*import net.minecraft.client.item.TooltipProvider;
*///?}
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.doll.data.TotemDollData;
import com.darkz.skintotem.doll.manager.TotemDollManager;
import com.darkz.skintotem.doll.renderer.TotemDollRenderer;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.gui.tooltip.combined.CombinedTooltipProvider;
import com.darkz.skintotem.gui.tooltip.state.LoadingStateTooltipProvider;
import com.darkz.skintotem.gui.tooltip.tags.*;
import com.darkz.skintotem.gui.tooltip.wrapped.WrappedTextTooltipProvider;
import com.darkz.skintotem.tag.manager.TagsManager;

import java.util.*;
import java.util.stream.Stream;

@Mixin(ItemStack.class)
@ExtensionMethod(ItemStackExtension.class)
public abstract class ItemStackMixin {

	@Shadow
	public abstract boolean isOf(Item item);

	@ModifyReturnValue(at = @At("RETURN"), method = "getName")
	private Component getName(Component original) {
		if (!SkinTotemModConfig.getInstance().isModEnabled() || !this.isOf(Items.TOTEM_OF_UNDYING)) {
			return original;
		}
		String string = original.getString();
		if (!string.contains("|")) {
			return original;
		}
		String[] data = TagsManager.getDataFromString(string);
		String name = data[0];
		String tags = data[1];
		if (tags == null || name == null) {
			return original;
		}
		return Component.literal(name).setStyle(original.getStyle());
	}

	@ModifyReturnValue(at = @At("RETURN"), method = "getTooltipProvider")
	private Optional<TooltipProvider> getTooltipProvider(Optional<TooltipProvider> original) {
		ItemStack itemStack = (ItemStack) (Object) this;

		if (!TotemDollRenderer.canRender(itemStack)) {
			return original;
		}

		Component customName = itemStack.getRealCustomName();
		if (customName == null) {
			return original;
		}

		String[] data = TagsManager.getDataFromString(customName.getString());

		Optional<TooltipProvider> loadingStateTooltipProvider = this.getLoadingStateTooltipProvider(data);
		Optional<TooltipProvider> tagsTooltipProvider = this.getTagsTooltipProvider(data);

		List<TooltipComponent> list = Stream.of(loadingStateTooltipProvider, tagsTooltipProvider)
				.flatMap(Optional::stream)
				.map(TooltipComponent::of)
				.toList();

		return Optional.of(new CombinedTooltipProvider(list));
	}

	@Unique
	private Optional<TooltipProvider> getLoadingStateTooltipProvider(String[] data) {
		Screen currentScreen = Minecraft.getInstance().currentScreen;
		if (!(currentScreen instanceof AnvilScreen || ScreenUtils.hasShiftDown())) {
			return Optional.empty();
		}
		if (data.length == 0) {
			return Optional.empty();
		}
		String o = data[0];
		TotemDollData totemDollData = TotemDollManager.getDoll(o);
		return Optional.of(new LoadingStateTooltipProvider(totemDollData.getStandardSprites().getState()));
	}

	@Unique
	private Optional<TooltipProvider> getTagsTooltipProvider(String[] data) {
		if (data.length < 2) {
			return Optional.empty();
		}
		String tags = data[1];
		if (tags == null || tags.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(new CombinedTooltipProvider(
					new WrappedTextTooltipProvider(SkinTotemMod.text("tags.title").formatted(ChatFormatting.GRAY)),
					new TagsTooltipProvider(tags)
				)
		);
	}

}
