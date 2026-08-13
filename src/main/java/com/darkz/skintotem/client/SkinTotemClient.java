package com.darkz.skintotem.client;

import com.darkz.skintotem.cache.KnownPlayerUUIDsConfigManager;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.Util;
import net.minecraft.Util.OS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.*;

import com.darkz.skintotem.*;
import com.darkz.skintotem.client.command.SkinTotemCommandManager;
import com.darkz.skintotem.client.event.SkinTotemEvents;


import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.loader.SkinTotemLoader;
import com.darkz.skintotem.pack.*;
import com.darkz.skintotem.tag.manager.*;
import com.darkz.skintotem.utils.plugin.SkinTotemPlugin;

import org.jetbrains.annotations.Nullable;

public class SkinTotemClient {

	public static Logger LOGGER = LoggerFactory.getLogger(SkinTotem.MOD_NAME + "/Client");

	public static void onInitializeClient() {
		LOGGER.info("{} Client Initialized", SkinTotem.MOD_NAME);
		TagsManager.register();
		TagsSkinProviders.register();
		SkinTotemLoader.registerCommands(SkinTotemCommandManager::register);
		SkinTotemEvents.register();
		SkinTotemReloadListener.register();
		SkinTotemPlugin.register();
		KnownPlayerUUIDsConfigManager.start();
	}

	public static boolean canProcess(@Nullable ItemStack stack) {
		return stack != null && SkinTotemConfig.getInstance().isModEnabled() && isProbablyTotem(stack);
	}

	@SuppressWarnings("deprecation")
	private static boolean isProbablyTotem(ItemStack stack) {
		return stack.item == Items.TOTEM_OF_UNDYING || (SkinTotemConfig.getInstance().isSupportOtherModsTotems() && BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().contains("totem"));
	}
}
