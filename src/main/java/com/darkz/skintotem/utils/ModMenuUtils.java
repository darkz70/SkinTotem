package com.darkz.skintotem.utils;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.yacl.custom.simple.utils.SimpleContent;

import java.util.function.Function;

public final class ModMenuUtils {

	private ModMenuUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String getOptionKey(String optionId) {
		return String.format("modmenu.option.%s", optionId);
	}

	public static String getCategoryKey(String categoryId) {
		return String.format("modmenu.category.%s", categoryId);
	}

	public static String getGroupKey(String groupId) {
		return String.format("modmenu.group.%s", groupId);
	}

	public static Text getName(String key) {
		return SkinTotemMod.text(key + ".name");
	}

	public static Text getDescription(String key) {
		return SkinTotemMod.text(key + ".description");
	}

	public static Identifier getContentId(SimpleContent content, String contentId) {
		return SkinTotemMod.id(String.format("textures/config/%s.%s", contentId, content.getFileExtension()));
	}

	public static Text getModTitle() {
		return SkinTotemMod.text("modmenu.title");
	}

	public static Function<Boolean, Text> getEnabledOrDisabledFormatter() {
		return state -> SkinTotemMod.text("modmenu.formatter.enabled_or_disabled." + state);
	}

	public static Text getNoConfigScreenMessage() {
		return SkinTotemMod.text("modmenu.no_config_library_screen.message");
	}

	public static Text getOldConfigScreenMessage(String version) {
		return SkinTotemMod.text("modmenu.old_config_library_screen.message", version, SkinTotemMod.YACL_DEPEND_VERSION);
	}
}
