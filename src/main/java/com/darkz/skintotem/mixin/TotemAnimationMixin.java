package com.darkz.skintotem.mixin;

import com.darkz.skintotem.client.SkinTotemTextureManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class TotemAnimationMixin {

    @Inject(method = "showFloatingItem(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
    private void skintotem$onTotemPop(ItemStack stack, CallbackInfo ci) {
        if (stack == null || !stack.isOf(Items.TOTEM_OF_UNDYING)) return;
        var nameComp = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (nameComp == null) return;
        String input = nameComp.getString();
        if (input != null && !input.isBlank()) {
            // Pre-warm texture for death animation
            SkinTotemTextureManager.getOrLoad(input);
        }
    }
}
