package com.darkz.skintotem.modmenu;

import com.terraformersmc.modmenu.api.*;

import net.fabricmc.loader.api.*;

import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.yacl.YACLConfigurationScreen;

public class MenuIntegration implements MenuApi {

	@Override
	public ConfigScreenFactory<?> getConfigScreenFactory() {
		FabricLoader fabricLoader = FabricLoader.getInstance();
		if (fabricLoader.isLoaded("yet_another_config_lib_v3")) {
			Container modContainer = fabricLoader.getContainer("yet_another_config_lib_v3").orElseThrow();
			Version version = modContainer.getMetadata().getVersion();
			try {
				Version requestsVersion = Version.parse(SkinTotem.YACL_DEPEND_VERSION);
				if (version.compareTo(requestsVersion) >= 0) {
					return YACLConfigurationScreen::createScreen;
				}
			} catch (VersionParsingException e) {
				SkinTotemClient.LOGGER.error("Failed to compare YACL version, tell mod author about this error: ", e);
			}
			return parent -> NoConfigLibraryScreen.createScreenAboutOldVersion(parent, version.getFriendlyString());
		}
		return NoConfigLibraryScreen::createScreen;
	}
}
