package com.darkz.skintotem.mixin;

import com.darkz.skintotem.client.SkinTotemTextureManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class TotemModelMixin {

    @Shadow
    private BakedModelManager bakedModelManager;

    /**
     * Intercept getModel — return a SkinTotemBakedModel wrapping the dynamic texture
     * when the totem has a custom name (nick or URL).
     */
    @Inject(
        method = "getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;",
        at = @At("RETURN"),
        cancellable = true
    )
    private void skintotem$getModel(
            ItemStack stack,
            net.minecraft.world.World world,
            net.minecraft.entity.LivingEntity entity,
            int seed,
            CallbackInfoReturnable<BakedModel> cir) {

        if (stack == null || !stack.isOf(Items.TOTEM_OF_UNDYING)) return;

        var nameComp = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (nameComp == null) return;

        String input = nameComp.getString();
        if (input == null || input.isBlank()) return;

        Identifier tex = SkinTotemTextureManager.getOrLoad(input);
        if (tex == null) return; // still loading — use default model

        BakedModel original = cir.getReturnValue();
        if (original == null) return;

        // Wrap original model with our texture override
        cir.setReturnValue(new SkinTotemBakedModel(original, tex));
    }
}
