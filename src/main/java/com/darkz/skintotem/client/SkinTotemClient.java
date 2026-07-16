package com.darkz.skintotem.client;

import com.darkz.skintotem.cache.KnownPlayerUUIDsConfigManager;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Util;
import net.minecraft.util.Util.OperatingSystem;
import org.slf4j.*;
import net.fabricmc.api.ClientInitializer;

import com.darkz.skintotem.*;
import com.darkz.skintotem.client.command.SkinTotemCommandManager;
import com.darkz.skintotem.client.event.SkinTotemEvents;


import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.pack.*;
import com.darkz.skintotem.tag.manager.*;
import com.darkz.skintotem.utils.plugin.SkinTotemPlugin;
import com.darkz.skintotem.refresh.SkinAutoRefresher;

import org.jetbrains.annotations.Nullable;

public class SkinTotemClient implements ClientInitializer {

	public static Logger LOGGER = LoggerFactory.getLogger(SkinTotem.MOD_NAME + "/Client");

	@Override
	public void onInitializeClient() {
		LOGGER.info("{} Client Initialized", SkinTotem.MOD_NAME);
		TagsManager.register();
		TagsSkinProviders.register();
		SkinTotemCommandManager.register();
		SkinTotemEvents.register();
		SkinTotemReloadListener.register();
		SkinTotemPlugin.register();
		KnownPlayerUUIDsConfigManager.start();
		SkinAutoRefresher.start();
		//? if >=1.21.6 {
		net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry.register(
				context -> new com.darkz.skintotem.doll.renderer.special.ItemGuiElementRenderer(context.vertexConsumers()));
		//?}
	}

	public static boolean canProcess(@Nullable ItemStack stack) {
		return stack != null && SkinTotemConfig.getInstance().isEnabled() && isProbablyTotem(stack);
	}

	private static boolean isProbablyTotem(ItemStack stack) {
		return stack.getItem() == Items.TOTEM_OF_UNDYING || (SkinTotemConfig.getInstance().isSupportOthersTotems() && Registries.ITEM.getId(stack.getItem()).getPath().contains("totem"));
	}
}
