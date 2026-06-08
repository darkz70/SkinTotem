package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.world.item.*;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.extension.ItemStackExtension;

@Mixin(AnvilMenu.class)
@ExtensionMethod(ItemStackExtension.class)
public class AnvilMenuMixin {

	@WrapOperation(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getName()Lnet/minecraft/text/Component;"),
			method = "updateResult"
	)
	private Component swapItemName(ItemStack stack, Operation<Component> original) {
		if (!SkinTotemModClient.canProcess(stack)) {
			return original.call(stack);
		}
		Component customName = stack.getRealCustomName();
		if (customName == null) {
			return original.call(stack);
		}
		return customName;
	}

}
