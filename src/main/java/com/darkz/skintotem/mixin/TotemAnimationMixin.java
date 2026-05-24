package com.darkz.skintotem.mixin;

import com.darkz.skintotem.SkinTotemCache;
import com.darkz.skintotem.SkinTotemMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks into the totem-of-undying death animation to use the custom skin texture.
 * The animation normally uses minecraft:textures/item/totem_of_undying.png;
 * here we point it to our dynamic texture if available.
 */
@Mixin(GameRenderer.class)
public class TotemAnimationMixin {

    @Inject(method = "showFloatingItem", at = @At("HEAD"))
    private void skintotem$onTotemAnimation(net.minecraft.item.ItemStack floatingItem, CallbackInfo ci) {
        if (floatingItem == null || !floatingItem.isOf(Items.TOTEM_OF_UNDYING)) return;

        var nameComponent = floatingItem.get(DataComponentTypes.CUSTOM_NAME);
        if (nameComponent == null) return;

        String input = nameComponent.getString();
        if (input == null || input.isBlank()) return;

        String key = input.trim().toLowerCase();

        // Ensure the texture is fetched and registered
        if (!SkinTotemCache.hasCached(key)) {
            SkinTotemCache.getOrFetch(input).thenAccept(img -> {
                if (img != null) {
                    SkinTotemMod.LOGGER.debug("Skin loaded for totem animation: {}", key);
                }
            });
        }
    }
}
