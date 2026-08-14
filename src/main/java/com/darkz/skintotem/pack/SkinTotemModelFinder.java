package com.darkz.skintotem.pack;

import net.minecraft.server.packs.resources.*;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.loader.SkinTotemLoader;

import java.util.*;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class SkinTotemModelFinder {

	private static final Set<ResourceLocation> BUILTIN_TOTEM_MODELS = new LinkedHashSet<>();
	private static final Map<String, Set<ResourceLocation>> FOUNDED_TOTEM_MODELS = new LinkedHashMap<>();

	public static Map<String, Set<ResourceLocation>> getFoundedTotemModels() {
		return FOUNDED_TOTEM_MODELS;
	}

	public static Set<ResourceLocation> getBuiltinTotemModels() {
		return BUILTIN_TOTEM_MODELS;
	}

	public static void reload(ResourceManager resourceManager) {
		List<PackResources> list = resourceManager.listPacks().filter(resourcePack -> resourcePack.getNamespaces(PackType.CLIENT_RESOURCES).contains(SkinTotem.MOD_ID)).toList();

		FOUNDED_TOTEM_MODELS.clear();
		for (PackResources pack : list) {
			String packId = pack.packId().replace("file/", "").replace("mod/", "");
			if (packId.equals(SkinTotem.MOD_ID) || SkinTotemLoader.isModResourcePack(pack)) {
				continue;
			}
			pack.listResources(PackType.CLIENT_RESOURCES, SkinTotem.MOD_ID, "dolls", (id, input) -> {
				if (!isModelPath(id)) {
					return;
				}

				Set<ResourceLocation> set = FOUNDED_TOTEM_MODELS.getOrDefault(packId, new LinkedHashSet<>());
				set.add(id);

				if (!FOUNDED_TOTEM_MODELS.containsKey(packId)) {
					FOUNDED_TOTEM_MODELS.put(packId, set);
				}
			});
		}
	}

	private static boolean isModelPath(ResourceLocation id) {
		return id.getPath().endsWith(".bbmodel");
	}
}
