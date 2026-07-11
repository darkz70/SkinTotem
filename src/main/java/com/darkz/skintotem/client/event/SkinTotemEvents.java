package com.darkz.skintotem.client.event;

import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.atlas.manager.*;
import com.darkz.skintotem.gui.tooltip.combined.*;
import com.darkz.skintotem.gui.tooltip.info.*;
import com.darkz.skintotem.gui.tooltip.preview.*;
import com.darkz.skintotem.gui.tooltip.state.LoadingStateTooltipData;
import com.darkz.skintotem.gui.tooltip.tags.*;
import com.darkz.skintotem.gui.tooltip.wrapped.*;
import com.darkz.skintotem.loader.SkinTotemLoader;
import com.darkz.skintotem.thread.SkinTotemTaskExecutor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public class SkinTotemEvents {

	public static void register() {
		registerTooltipCallbacks();
		registerLifecycleEvents();
	}

	private static void registerTooltipCallbacks() {
		SkinTotemLoader.registerTooltipComponentFactory(TagsTooltipData.class, (data) -> new TagsTooltipComponent(data.tags()));
		SkinTotemLoader.registerTooltipComponentFactory(InfoTooltipData.class, (data) -> new InfoTooltipComponent(data.key(), data.color()));
		SkinTotemLoader.registerTooltipComponentFactory(LoadingStateTooltipData.class, (data) -> ClientTooltipComponent.create(SkinTotem.text("text.status").append(data.state().getText()).getVisualOrderText()));
		SkinTotemLoader.registerTooltipComponentFactory(CombinedTooltipData.class, (data) -> new CombinedTooltipComponent(data.list()));
		SkinTotemLoader.registerTooltipComponentFactory(TotemDollPreviewTooltipData.class, (data) -> new TotemDollPreviewTooltipComponent(data.data(), data.model()));
		SkinTotemLoader.registerTooltipComponentFactory(WrappedTextTooltipData.class, (data) -> new WrappedTextTooltipComponent(data.text()));
	}

	private static void registerLifecycleEvents() {
		SkinTotemLoader.registerClientStopping(() -> {
			SkinTotemTaskExecutor.stop();
			SkinTotemAtlasManager.close();
			SkinTotemAtlasSpriteManager.close();
		});
	}
}
