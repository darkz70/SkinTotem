package com.darkz.skintotem.client.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import com.darkz.skintotem.atlas.manager.*;
import net.minecraft.client.gui.tooltip.*;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.gui.tooltip.combined.*;
import com.darkz.skintotem.gui.tooltip.info.*;
import com.darkz.skintotem.gui.tooltip.preview.*;
import com.darkz.skintotem.gui.tooltip.state.LoadingStateTooltipData;
import com.darkz.skintotem.gui.tooltip.tags.*;
import com.darkz.skintotem.gui.tooltip.wrapped.*;
import com.darkz.skintotem.thread.SkinTotemModTaskExecutor;

public class SkinTotemModEvents {

	public static void register() {
		registerTooltipCallbacks();
		registerLifecycleEvents();
	}

	private static void registerTooltipCallbacks() {
		TooltipComponentCallback.EVENT.register((data -> {
			if (data instanceof TagsTooltipData tooltipData) {
				return new TagsTooltipComponent(tooltipData.tags());
			}
			if (data instanceof InfoTooltipData tooltipData) {
				return new InfoTooltipComponent(tooltipData.key(), tooltipData.color());
			}
			if (data instanceof LoadingStateTooltipData tooltipData) {
				return TooltipComponent.of(SkinTotemMod.text("text.status").append(tooltipData.state().getText()).asOrderedText());
			}
			if (data instanceof CombinedTooltipData tooltipData) {
				return new CombinedTooltipComponent(tooltipData.list());
			}
			if (data instanceof SkinTotemPreviewTooltipData tooltipData) {
				return new SkinTotemPreviewTooltipComponent(tooltipData.data(), tooltipData.model());
			}
			if (data instanceof WrappedTextTooltipData tooltipData) {
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
