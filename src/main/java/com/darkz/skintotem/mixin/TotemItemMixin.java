package com.darkz.skintotem.mixin;

import com.darkz.skintotem.client.SkinTotemTextureManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public class TotemItemMixin {

    /**
     * HEAD inject — before rendering starts.
     * We kick off async skin fetch here if needed.
     * The actual texture swap happens in TotemModelMixin.
     */
    @Inject(
        method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At("HEAD"),
        cancellable = false
    )
    private void skintotem$onRenderItem(
            ItemStack stack,
            ModelTransformationMode mode,
            boolean leftHand,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay,
            BakedModel model,
            CallbackInfo ci) {

        if (stack == null || !stack.isOf(Items.TOTEM_OF_UNDYING)) return;

        var nameComp = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (nameComp == null) return;

        String input = nameComp.getString();
        if (input == null || input.isBlank()) return;

        // This triggers async fetch if not already loading/loaded
        Identifier tex = SkinTotemTextureManager.getOrLoad(input);
        // tex will be null on first call (loading), non-null once ready
        // The actual render override is done in TotemModelMixin
    }
}
