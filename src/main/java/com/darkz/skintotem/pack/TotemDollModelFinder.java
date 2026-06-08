package com.darkz.skintotem.pack;

import net.minecraft.server.packs.resources.*;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.SkinTotemMod;

import java.util.*;

public class TotemDollModelFinder {

	private static final Set<Identifier> BUILTIN_TOTEM_MODELS = new LinkedHashSet<>();
	private static final Map<String, Set<Identifier>> FOUNDED_TOTEM_MODELS = new LinkedHashMap<>();

	public static Map<String, Set<Identifier>> getFoundedTotemModels() {
		return FOUNDED_TOTEM_MODELS;
	}

	public static Set<Identifier> getBuiltinTotemModels() {
		return BUILTIN_TOTEM_MODELS;
	}

	public static void reload(ResourceManager resourceManager) {
		List<PackResources> list = resourceManager.streamPackResourcess().filter(resourcePack -> resourcePack.getNamespaces(PackType.CLIENT_RESOURCES).contains(SkinTotemMod.MOD_ID)).toList();

		FOUNDED_TOTEM_MODELS.clear();
		for (PackResources pack : list) {
			String packId = pack./*? if >=1.21 {*/getId()/*?} else {*//*getName()*//*?}*/.replace("file/", "");
		if (packId.equals(SkinTotemMod.MOD_ID) /*? if =1.20.1 {*/ /*|| pack instanceof net.fabricmc.fabric.impl.resource.loader.FabricModPackResources *//*?}*/) {
				continue;
			}
			pack.findResources(PackType.CLIENT_RESOURCES, SkinTotemMod.MOD_ID, "dolls", (id, input) -> {
				if (!isModelPath(id)) {
					return;
				}

				Set<Identifier> set = FOUNDED_TOTEM_MODELS.getOrDefault(packId, new LinkedHashSet<>());
				set.add(id);

				if (!FOUNDED_TOTEM_MODELS.containsKey(packId)) {
					FOUNDED_TOTEM_MODELS.put(packId, set);
				}
			});
		}
	}

	private static boolean isModelPath(Identifier id) {
		return id.getPath().endsWith(".bbmodel");
	}
}
