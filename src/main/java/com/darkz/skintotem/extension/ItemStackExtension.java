package com.darkz.skintotem.extension;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
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
		return itemStack.components.get(net.minecraft.component.DataComponentTypes.CUSTOM_NAME);
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

	public static SkinTotemData getSkinTotemData(ItemStack stack) {
		return getSkinTotemData(stack, true);
	}

	public static SkinTotemData getSkinTotemData(ItemStack stack, boolean applyRenderProperties) {
		Text name = getRealCustomName(stack);

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

	public static void setdedModel(ItemStack itemStack, boolean modded) {
		((ItemStackWithdedBakedModel) itemStack).skinTotem$setdedModel(modded);
	}

	public static boolean hasdedModel(ItemStack itemStack) {
		return ((ItemStackWithdedBakedModel) itemStack).skinTotem$isdedModel();
	}

	public static void setPlayerEntity(ItemStack itemStack, AbstractClientPlayerEntity playerEntity) {
		((ItemStackWithPlayerEntity) itemStack).skinTotem$setPlayerEntity(playerEntity);
	}

	public static AbstractClientPlayerEntity getPlayerEntity(ItemStack itemStack) {
		return ((ItemStackWithPlayerEntity) itemStack).skinTotem$getPlayerEntity();
	}

}
