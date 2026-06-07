package com.darkz.skintotem.extension;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.doll.manager.*;
import com.darkz.skintotem.tag.manager.TagsManager;
import com.darkz.skintotem.utils.mixin.*;

import org.jetbrains.annotations.Nullable;

public class ItemStackExtension {

	@Nullable
	public static Text getRealCustomName(ItemStack itemStack) {
		//? if >=1.21 {
		if (itemStack.components == null) {
			return null;
		}
		return itemStack.components.get(net.minecraft.component.DataComponents.CUSTOM_NAME);
		//?} else {
		/*net.minecraft.nbt.NbtCompound nbtCompound = itemStack.getSubNbt("display");
		if (nbtCompound != null && nbtCompound.contains("Name", 8)) {
			try {
				Text text = net.minecraft.text.Text.Serializer.fromJson(nbtCompound.getString("Name"));
				if (text != null) {
					return text;
				}

				nbtCompound.remove("Name");
			} catch (Exception var3) {
				nbtCompound.remove("Name");
			}
		}

		return null;
		*///?}
	}

	public static TotemDollData getTotemDollData(ItemStack stack) {
		return getTotemDollData(stack, true);
	}

	public static TotemDollData getTotemDollData(ItemStack stack, boolean applyRenderProperties) {
		Text name = getRealCustomName(stack);

		if (name != null) {
			String o = TagsManager.getNicknameOrSkinProviderFromName(name.getString());
			TotemDollData data = TotemDollManager.getDoll(o);

			// refresh render properties
			data.refreshRenderProperties();

			String tags = TagsManager.getTagsFromName(name.getString());
			if (tags != null) {
				// Editing render properties here
				TagsManager.processTags(tags, data);
			}

			return applyRenderProperties ? data.applyRenderProperties() : data; // apply render properties
		}

		TotemDollData data = StandardTotemDollManager.getStandardDoll().refreshRenderProperties();
		return applyRenderProperties ? data.applyRenderProperties() : data;
	}

	public static void setModdedModel(ItemStack itemStack, boolean modded) {
		((ItemStackWithModdedBakedModel) itemStack).myTotemDoll$setModdedModel(modded);
	}

	public static boolean hasModdedModel(ItemStack itemStack) {
		return ((ItemStackWithModdedBakedModel) itemStack).myTotemDoll$isModdedModel();
	}

	public static void setPlayerEntity(ItemStack itemStack, AbstractClientPlayerEntity playerEntity) {
		((ItemStackWithPlayerEntity) itemStack).myTotemDoll$setPlayerEntity(playerEntity);
	}

	public static AbstractClientPlayerEntity getPlayerEntity(ItemStack itemStack) {
		return ((ItemStackWithPlayerEntity) itemStack).myTotemDoll$getPlayerEntity();
	}

}
