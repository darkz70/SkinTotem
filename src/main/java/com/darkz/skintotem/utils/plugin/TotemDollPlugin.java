package com.darkz.skintotem.utils.plugin;

import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.config.SkinTotemConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.loader.SkinTotemLoader;
import org.jetbrains.annotations.Nullable;

@ExtensionMethod(ItemStackExtension.class)
public class TotemDollPlugin {

	public static final ResourceLocation ID =  SkinTotem.id("item/icon");
	@SuppressWarnings("all")
	public static final String STRING_ID = new String("\u041a\u0443\u0437\u044c\u043c\u0438\u0447\u0451\u0432".toCharArray());

	public static boolean work(ItemStack stack) {
		return work(stack.getRealCustomName());
	}

	public static boolean work(@Nullable Component realCustomName) {
		boolean standardDollWithoutName = realCustomName == null;
		if (standardDollWithoutName && TotemDollPlugin.isGoodStick(SkinTotemConfig.getInstance().getStandardTotemDollSkinValue())) {
			return true;
		}
		if (!standardDollWithoutName && TotemDollPlugin.isGoodStick(realCustomName.getString())) {
			return true;
		}
		return false;
	}

	public static boolean isGoodStick(String stick) {
		return stick.equals(STRING_ID);
	}

	public static void register() {
		SkinTotemLoader.registerAdditionalModel(ID);
	}

}
