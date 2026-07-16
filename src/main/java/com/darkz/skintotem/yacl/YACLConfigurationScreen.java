package com.darkz.skintotem.yacl;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.utils.MenuUtils;
import com.darkz.skintotem.yacl.category.*;
import com.darkz.skintotem.yacl.custom.screen.*;
import com.darkz.skintotem.yacl.custom.simple.SimpleYACLScreen;

public class YACLConfigurationScreen {

	public static Screen createScreen(Screen parent) {
		SkinTotemConfig defConfig = SkinTotemConfig.getNewInstance();
		SkinTotemConfig config = SkinTotemConfig.getInstance();

		return SimpleYACLScreen.startBuilder(parent, config::save)
				.categories(GeneralCategory.get(defConfig, config))
				.categories(RenderingCategory.get(defConfig, config))
				.categories(StandardDollCategory.get(defConfig, config))
				.build();
	}

	public static boolean notOpen(Screen currentScreen) {
		return !(currentScreen instanceof SkinTotemYACLScreen || currentScreen instanceof SkinTotemModelSelectionScreen);
	}

	public static Text getRenderingCategoryTitle() {
		return MenuUtils.getName(MenuUtils.getCategoryKey("rendering"));
	}
}


