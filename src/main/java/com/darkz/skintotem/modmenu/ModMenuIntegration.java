package com.darkz.skintotem.modmenu;

//? if fabric {

import com.terraformersmc.modmenu.api.*;

import net.fabricmc.loader.api.*;

import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.yacl.YACLConfigurationScreen;

public class ModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		FabricLoader fabricLoader = FabricLoader.getInstance();
		if (fabricLoader.isModLoaded("yet_another_config_lib_v3")) {
			ModContainer modContainer = fabricLoader.getModContainer("yet_another_config_lib_v3").orElseThrow();
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

//?} elif forge {

/*import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.loader.SkinTotemLoader;
import com.darkz.skintotem.yacl.YACLConfigurationScreen;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.fml.*;
import org.apache.maven.artifact.versioning.*;

public class ModMenuIntegration {

	public void register(ModContainer container) {
		container.registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((minecraft, parent) -> {
			if (SkinTotemLoader.isModLoaded("yet_another_config_lib_v3", false)) {
				ModContainer yacl = ModList.get().getModContainerById("yet_another_config_lib_v3").orElseThrow();
				ArtifactVersion version = yacl.getModInfo().getVersion();
				try {
					ArtifactVersion requestsVersion = new DefaultArtifactVersion(SkinTotem.YACL_DEPEND_VERSION);
					if (version.compareTo(requestsVersion) >= 0) {
						return YACLConfigurationScreen.createScreen(parent);
					}
				} catch (Exception e) {
					SkinTotemClient.LOGGER.error("Failed to compare YACL version, tell mod author about this error: ", e);
				}
				return NoConfigLibraryScreen.createScreenAboutOldVersion(parent, version.getQualifier());
			}
			return NoConfigLibraryScreen.createScreen(parent);
		}));
	}
}

*///?}
