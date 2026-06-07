package com.darkz.skintotem.yacl;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.chat.Component;

import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.config.SkinTotemModConfig;
import com.darkz.skintotem.utils.ModMenuUtils;
import com.darkz.skintotem.yacl.category.*;
import com.darkz.skintotem.yacl.custom.screen.*;
import com.darkz.skintotem.yacl.custom.simple.SimpleYACLScreen;

public class YACLConfigurationScreen {

	public static Screen createScreen(Screen parent) {
		SkinTotemModConfig defConfig = SkinTotemModConfig.getNewInstance();
		SkinTotemModConfig config = SkinTotemModConfig.getInstance();

		return SimpleYACLScreen.startBuilder(parent, config::save)
				.categories(GeneralCategory.get(defConfig, config))
				.categories(RenderingCategory.get(defConfig, config))
				.categories(StandardDollCategory.get(defConfig, config))
				.build();
	}

	public static boolean notOpen(Screen currentScreen) {
		return !(currentScreen instanceof SkinTotemModYACLScreen || currentScreen instanceof TotemDollModelSelectionScreen);
	}

	public static Text getRenderingCategoryTitle() {
		return ModMenuUtils.getName(ModMenuUtils.getCategoryKey("rendering"));
	}
}


