package com.darkz.skintotem.mixin;

import com.darkz.skintotem.client.SkinTotemTextureManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class TotemAnimationMixin {

    @Inject(method = "showFloatingItem(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
    private void skintotem$onTotemPop(ItemStack stack, CallbackInfo ci) {
        if (stack == null || !stack.isOf(Items.TOTEM_OF_UNDYING)) return;
        var name = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (name == null) return;
        String input = name.getString();
        if (input != null && !input.isBlank()) {
            SkinTotemTextureManager.getOrLoad(input);
        }
    }
}
