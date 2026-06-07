package com.darkz.skintotem.client;

import com.darkz.skintotem.cache.KnownPlayerUUIDsConfigManager;
//? if >=26.1 {
import net.minecraft.world.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Util;
import net.minecraft.util.Util.OperatingSystem;
*///?}
import org.slf4j.*;
import net.fabricmc.api.ClientModInitializer;

import com.darkz.skintotem.*;
import com.darkz.skintotem.client.command.SkinTotemModCommandManager;
import com.darkz.skintotem.client.event.SkinTotemModEvents;

import com.darkz.skintotem.config.SkinTotemModConfig;
import com.darkz.skintotem.pack.*;
import com.darkz.skintotem.tag.manager.*;
import com.darkz.skintotem.utils.plugin.TotemDollPlugin;

import org.jetbrains.annotations.Nullable;

public class SkinTotemModClient implements ClientModInitializer {

	public static Logger LOGGER = LoggerFactory.getLogger(SkinTotemMod.MOD_NAME + "/Client");

	@Override
	public void onInitializeClient() {
		LOGGER.info("{} Client Initialized", SkinTotemMod.MOD_NAME);
		TagsManager.register();
		TagsSkinProviders.register();
		SkinTotemModCommandManager.register();
		SkinTotemModEvents.register();
		SkinTotemModReloadListener.register();
		TotemDollPlugin.register();
		KnownPlayerUUIDsConfigManager.start();
		//? if >=1.21.6 {
		net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry.register(
				context -> new com.darkz.skintotem.doll.renderer.special.ItemGuiElementRenderer(context.vertexConsumers()));
		//?}
	}

	public static boolean canProcess(@Nullable ItemStack stack) {
		return stack != null && SkinTotemModConfig.getInstance().isModEnabled() && isProbablyTotem(stack);
	}

	@SuppressWarnings("deprecation")
	private static boolean isProbablyTotem(ItemStack stack) {
		//? if >=26.1 {
		boolean bl = stack.item != null && stack.item.value() == Items.TOTEM_OF_UNDYING;
		return bl || (SkinTotemModConfig.getInstance().isSupportOtherModsTotems() && BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().contains("totem"));
		//?} else {
		/*return stack.getItem() == Items.TOTEM_OF_UNDYING || (SkinTotemModConfig.getInstance().isSupportOtherModsTotems() && Registries.ITEM.getId(stack.getItem()).getPath().contains("totem"));
		*///?}
	}
}
