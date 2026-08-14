package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.doll.data.SkinTotemData;
import com.darkz.skintotem.doll.manager.SkinTotemManager;
import com.darkz.skintotem.doll.renderer.SkinTotemRenderer;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.gui.tooltip.combined.CombinedTooltipData;
import com.darkz.skintotem.gui.tooltip.state.LoadingStateTooltipData;
import com.darkz.skintotem.gui.tooltip.tags.*;
import com.darkz.skintotem.gui.tooltip.wrapped.WrappedTextTooltipData;
import com.darkz.skintotem.tag.manager.TagsManager;

import java.util.*;
import java.util.stream.Stream;

@Mixin(ItemStack.class)
@ExtensionMethod(ItemStackExtension.class)
public abstract class ItemStackMixin {

	@Shadow
	public abstract boolean is(Item item);

	@ModifyReturnValue(at = @At("RETURN"), method = "getHoverName")
	private Component getName(Component original) {
		if (!SkinTotemConfig.getInstance().isModEnabled() || !this.is(Items.TOTEM_OF_UNDYING)) {
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

	@ModifyReturnValue(at = @At("RETURN"), method = "getTooltipImage")
	private Optional<TooltipComponent> getTooltipData(Optional<TooltipComponent> original) {
		ItemStack itemStack = (ItemStack) (Object) this;

		if (!SkinTotemRenderer.canRender(itemStack)) {
			return original;
		}

		Component customName = itemStack.getRealCustomName();
		if (customName == null) {
			return original;
		}

		String[] data = TagsManager.getDataFromString(customName.getString());

		Optional<TooltipComponent> loadingStateTooltipData = this.mySkinTotem$getLoadingStateTooltipData(data);
		Optional<TooltipComponent> tagsTooltipData = this.mySkinTotem$getTagsTooltipData(data);

		List<ClientTooltipComponent> list = Stream.of(loadingStateTooltipData, tagsTooltipData)
				.flatMap(Optional::stream)
				.map(ClientTooltipComponent::create)
				.toList();

		return Optional.of(new CombinedTooltipData(list));
	}

	@Unique
	private Optional<TooltipComponent> mySkinTotem$getLoadingStateTooltipData(String[] data) {
		Screen currentScreen = Minecraft.getInstance().screen;
		if (!(currentScreen instanceof AnvilScreen || ScreenUtils.hasShiftDown())) {
			return Optional.empty();
		}
		if (data.length == 0) {
			return Optional.empty();
		}
		String o = data[0];
		SkinTotemData totemDollData = SkinTotemManager.getDoll(o);
		return Optional.of(new LoadingStateTooltipData(totemDollData.getStandardSprites().getState()));
	}

	@Unique
	private Optional<TooltipComponent> mySkinTotem$getTagsTooltipData(String[] data) {
		if (data.length < 2) {
			return Optional.empty();
		}
		String tags = data[1];
		if (tags == null || tags.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(new CombinedTooltipData(
					new WrappedTextTooltipData(SkinTotem.text("tags.title").withStyle(ChatFormatting.GRAY)),
					new TagsTooltipData(tags)
				)
		);
	}

}
