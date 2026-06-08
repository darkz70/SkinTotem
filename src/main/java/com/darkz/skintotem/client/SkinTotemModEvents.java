package com.darkz.skintotem.client.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import com.darkz.skintotem.atlas.manager.*;
import net.minecraft.client.gui.screens.inventory.tooltip.*;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ClientClientTooltipComponentCallback;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.gui.tooltip.combined.*;
import com.darkz.skintotem.gui.tooltip.info.*;
import com.darkz.skintotem.gui.tooltip.preview.*;
import com.darkz.skintotem.gui.tooltip.state.LoadingStateTooltipProvider;
import com.darkz.skintotem.gui.tooltip.tags.*;
import com.darkz.skintotem.gui.tooltip.wrapped.*;
import com.darkz.skintotem.thread.SkinTotemModTaskExecutor;

public class SkinTotemModEvents {

	public static void register() {
		registerTooltipCallbacks();
		registerLifecycleEvents();
	}

	private static void registerTooltipCallbacks() {
		ClientTooltipComponentCallback.EVENT.register((data -> {
			if (data instanceof TagsTooltipProvider tooltipData) {
				return new TagsTooltipComponent(tooltipData.tags());
			}
			if (data instanceof InfoTooltipProvider tooltipData) {
				return new InfoTooltipComponent(tooltipData.key(), tooltipData.color());
			}
			if (data instanceof LoadingStateTooltipProvider tooltipData) {
				return ClientTooltipComponent.create(SkinTotemMod.text("text.status").append(tooltipData.state().getText()).getVisualOrderText());
			}
			if (data instanceof CombinedTooltipProvider tooltipData) {
				return new CombinedTooltipComponent(tooltipData.list());
			}
			if (data instanceof TotemDollPreviewTooltipProvider tooltipData) {
				return new TotemDollPreviewTooltipComponent(tooltipData.data(), tooltipData.model());
			}
			if (data instanceof WrappedTextTooltipProvider tooltipData) {
				return new WrappedTextTooltipComponent(tooltipData.text());
			}
			return null;
		}));
	}

	private static void registerLifecycleEvents() {
		ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> {
			SkinTotemModTaskExecutor.stop();
			SkinTotemModAtlasManager.close();
			SkinTotemModAtlasSpriteManager.close();
		});
	}
}
