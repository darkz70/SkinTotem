package com.darkz.skintotem.tag.manager;

import com.darkz.skintotem.doll.data.SkinTotemData;


import com.darkz.skintotem.doll.manager.StandardSkinTotemManager;
import com.darkz.skintotem.skin.provider.SkinProvider;
import com.darkz.skintotem.skin.provider.extended.NameMCSkinProvider;
import com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider;
import com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider;
import com.darkz.skintotem.skin.provider.extended.UrlSkinProvider;
import java.util.*;
import org.jetbrains.annotations.Nullable;


public class TagsSkinProviders {

	private static final Map<String, SkinProvider> SKIN_PROVIDERS_IDS = new HashMap<>();

	public static Map<String, SkinProvider> getSkinProvidersIds() {
		return SKIN_PROVIDERS_IDS;
	}

	public static void register() {
		registerProvider("NameMC", NameMCSkinProvider.getInstance());
		registerProvider("ElyBy", ElyBySkinProvider.getInstance());
		registerProvider("TLauncher", TLauncherSkinProvider.getInstance());
		registerProvider("URL", UrlSkinProvider.getInstance());
	}

	public static void registerProvider(String id, SkinProvider provider) {
		SKIN_PROVIDERS_IDS.put(id, provider);
	}

	public static boolean isProvider(String o) {
		return getProviderFor(o) != null;
	}

	@Nullable
	public static SkinProvider getProviderFor(String o) {
		if (o == null) {
			return null;
		}
		int b = o.lastIndexOf("|");
		if (b != -1) {
			String id = o.substring(0, b).trim();
			return SKIN_PROVIDERS_IDS.get(id);
		}
		for (SkinProvider provider : SKIN_PROVIDERS_IDS.values()) {
			if (provider.canProcess(o)) {
				return provider;
			}
		}
		return null;
	}

	public static SkinTotemData loadDollFromProvider(String o) {
		if (o == null) {
			return StandardSkinTotemManager.getStandardDoll();
		}
		if (!o.contains("|")) {
			SkinProvider skinProvider = getProviderFor(o);
			return skinProvider == null
					? StandardSkinTotemManager.getStandardDoll()
					: skinProvider.getOrLoadDoll(o);
		}
		String[] split = o.split("\\|", 2);
		SkinProvider skinProvider = getProviderFor(o);
		if (skinProvider == null || split.length < 2) {
			return StandardSkinTotemManager.getStandardDoll();
		}
		return skinProvider.getOrLoadDoll(split[1].trim());
	}
}
