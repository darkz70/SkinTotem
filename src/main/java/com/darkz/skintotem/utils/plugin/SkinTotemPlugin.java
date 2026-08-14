package com.darkz.skintotem.utils.plugin;

import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.config.SkinTotemConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.extension.ItemStackExtension;
import org.jetbrains.annotations.Nullable;

@ExtensionMethod(ItemStackExtension.class)
public class SkinTotemPlugin {

	public static final ResourceLocation ID = /*? >=1.21.3 {*/SkinTotem.id("icon"); /*?} else {*/ /*SkinTotem.id("item/icon"); *//*?}*/
	@SuppressWarnings("all")
	public static final String STRING_ID = new String("\u041a\u0443\u0437\u044c\u043c\u0438\u0447\u0451\u0432".toCharArray());

	public static boolean work(ItemStack stack) {
		return work(stack.getRealCustomName());
	}

	public static boolean work(@Nullable Component realCustomName) {
		boolean standardDollWithoutName = realCustomName == null;
		if (standardDollWithoutName && SkinTotemPlugin.isGoodStick(SkinTotemConfig.getInstance().getStandardSkinTotemSkinValue())) {
			return true;
		}
		if (!standardDollWithoutName && SkinTotemPlugin.isGoodStick(realCustomName.getString())) {
			return true;
		}
		return false;
	}

	public static boolean isGoodStick(String stick) {
		return stick.equals(STRING_ID);
	}

	public static void register() {
		//? if <=1.21.4 {
		/*net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.register(context -> {
			context.addModels(ID);
		});
		*///?}
	}

}
