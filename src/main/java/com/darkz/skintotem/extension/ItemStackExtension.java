package com.darkz.skintotem.extension;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.doll.manager.*;
import com.darkz.skintotem.tag.manager.TagsManager;
import com.darkz.skintotem.utils.mixin.*;

import org.jetbrains.annotations.Nullable;

public class ItemStackExtension {

	@Nullable
	public static Component getRealCustomName(ItemStack itemStack) {
		net.minecraft.nbt.CompoundTag nbtCompound = itemStack.getTagElement("display");
		if (nbtCompound != null && nbtCompound.contains("Name", 8)) {
			try {
				Component text = net.minecraft.network.chat.Component.Serializer.fromJson(nbtCompound.getString("Name"));
				if (text != null) {
					return text;
				}

				nbtCompound.remove("Name");
			} catch (Exception var3) {
				nbtCompound.remove("Name");
			}
		}

		return null;
	}

	public static SkinTotemData getSkinTotemData(ItemStack stack) {
		return getSkinTotemData(stack, true);
	}

	public static SkinTotemData getSkinTotemData(ItemStack stack, boolean applyRenderProperties) {
		Component name = getRealCustomName(stack);

		if (name != null) {
			String o = TagsManager.getNicknameOrSkinProviderFromName(name.getString());
			SkinTotemData data = SkinTotemManager.getDoll(o);

			// refresh render properties
			data.refreshRenderProperties();

			String tags = TagsManager.getTagsFromName(name.getString());
			if (tags != null) {
				// Editing render properties here
				TagsManager.processTags(tags, data);
			}

			return applyRenderProperties ? data.applyRenderProperties() : data; // apply render properties
		}

		SkinTotemData data = StandardSkinTotemManager.getStandardDoll().refreshRenderProperties();
		return applyRenderProperties ? data.applyRenderProperties() : data;
	}

	public static void setModdedModel(ItemStack itemStack, boolean modded) {
		((ItemStackWithModdedBakedModel) itemStack).skinTotem$setModdedModel(modded);
	}

	public static boolean hasModdedModel(ItemStack itemStack) {
		return ((ItemStackWithModdedBakedModel) itemStack).skinTotem$isModdedModel();
	}

	public static void setPlayerEntity(ItemStack itemStack, AbstractClientPlayer playerEntity) {
		((ItemStackWithPlayerEntity) itemStack).skinTotem$setPlayerEntity(playerEntity);
	}

	public static AbstractClientPlayer getPlayerEntity(ItemStack itemStack) {
		return ((ItemStackWithPlayerEntity) itemStack).skinTotem$getPlayerEntity();
	}

}
