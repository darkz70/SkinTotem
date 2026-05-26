package com.darkz.skintotem.mixin;

import com.darkz.skintotem.client.SkinTotemCache;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Pre-warms the skin cache when the totem-of-undying death animation fires.
 * This is the only mixin in the entire mod — a simple @Inject HEAD with no ModifyArg/ModifyVariable.
 */
@Mixin(GameRenderer.class)
public class TotemAnimationMixin {

    @Inject(method = "showFloatingItem(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
    private void skintotem$prewarm(ItemStack stack, CallbackInfo ci) {
        if (stack == null || !stack.isOf(Items.TOTEM_OF_UNDYING)) return;
        var name = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (name == null) return;
        String input = name.getString();
        if (input != null && !input.isBlank()) {
            SkinTotemCache.getOrLoad(input); // ensure loaded before animation plays
        }
    }
}
