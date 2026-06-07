package com.darkz.skintotem.pack;

import java.util.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

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
		FOUNDED_TOTEM_MODELS.clear();
		BUILTIN_TOTEM_MODELS.clear();

		for (Identifier id : resourceManager.findResources("models/totem", path -> path.getPath().endsWith(".json")).keySet()) {
			if (isModelPath(id)) {
				FOUNDED_TOTEM_MODELS.computeIfAbsent(id.getNamespace(), k -> new LinkedHashSet<>()).add(id);
			}
		}

		for (Identifier id : resourceManager.findResources("models/item/totem", path -> path.getPath().endsWith(".json")).keySet()) {
			if (isModelPath(id)) {
				BUILTIN_TOTEM_MODELS.add(id);
			}
		}
	}

	private static boolean isModelPath(Identifier id) {
		String path = id.getPath();
		return path.startsWith("models/") && path.endsWith(".json");
	}
}
