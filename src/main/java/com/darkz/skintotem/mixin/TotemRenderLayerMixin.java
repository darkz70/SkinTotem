package com.darkz.skintotem.mixin;

import com.darkz.skintotem.client.SkinTotemTextureManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Swaps the RenderLayer used for the totem item so it samples
 * our dynamically registered texture instead of the default totem PNG.
 *
 * We use a thread-local to pass the current item being rendered
 * into the ModifyArg that fires inside renderBakedItemModel.
 */
@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public class TotemRenderLayerMixin {

    private static final ThreadLocal<Identifier> CURRENT_SKIN_TEX = new ThreadLocal<>();

    @Inject(
        method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At("HEAD")
    )
    private void skintotem$captureStack(
            ItemStack stack,
            ModelTransformationMode mode,
            boolean leftHand,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay,
            BakedModel model,
            CallbackInfo ci) {

        CURRENT_SKIN_TEX.set(null);

        if (stack == null || !stack.isOf(Items.TOTEM_OF_UNDYING)) return;
        var nameComp = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (nameComp == null) return;
        String input = nameComp.getString();
        if (input == null || input.isBlank()) return;

        Identifier tex = SkinTotemTextureManager.getOrLoad(input);
        if (tex != null) {
            CURRENT_SKIN_TEX.set(tex);
        }
    }

    @Inject(
        method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At("RETURN")
    )
    private void skintotem$clearStack(
            ItemStack stack,
            ModelTransformationMode mode,
            boolean leftHand,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay,
            BakedModel model,
            CallbackInfo ci) {
        CURRENT_SKIN_TEX.set(null);
    }

    /**
     * When renderBakedItemModel calls getRenderLayer/getArmorVertexConsumer,
     * we swap the RenderLayer to use our texture.
     */
    @ModifyArg(
        method = "renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/RenderLayer;getItemEntityTranslucentCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
        ),
        index = 0
    )
    private Identifier skintotem$replaceTexture(Identifier original) {
        Identifier skin = CURRENT_SKIN_TEX.get();
        return skin != null ? skin : original;
    }
}
