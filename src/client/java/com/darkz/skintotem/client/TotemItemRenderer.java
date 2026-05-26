package com.darkz.skintotem.client;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class TotemItemRenderer implements DynamicItemRenderer {

    private static final Identifier DEFAULT_TOTEM =
            Identifier.of("minecraft", "textures/item/totem_of_undying.png");

    @Override
    public void render(ItemStack stack,
                       ModelTransformationMode mode,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       int overlay) {

        Identifier texId = DEFAULT_TOTEM;

        var nameComp = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (nameComp != null) {
            String input = nameComp.getString();
            if (input != null && !input.isBlank()) {
                Identifier skinTex = SkinTotemCache.getOrLoad(input);
                if (skinTex != null) texId = skinTex;
            }
        }

        matrices.push();
        matrices.translate(0.5f, 0.5f, 0.5f);

        VertexConsumer vc = vertexConsumers.getBuffer(
                RenderLayer.getItemEntityTranslucentCull(texId));
        Matrix4f m = matrices.peek().getPositionMatrix();

        v(vc, m, -0.5f,  0.5f, 0f, 0f, 0f, overlay, light);
        v(vc, m, -0.5f, -0.5f, 0f, 0f, 1f, overlay, light);
        v(vc, m,  0.5f, -0.5f, 0f, 1f, 1f, overlay, light);
        v(vc, m,  0.5f,  0.5f, 0f, 1f, 0f, overlay, light);

        matrices.pop();
    }

    private static void v(VertexConsumer vc, Matrix4f m,
                           float x, float y, float z,
                           float u, float v, int overlay, int light) {
        vc.vertex(m, x, y, z)
          .color(255, 255, 255, 255)
          .texture(u, v)
          .overlay(overlay)
          .light(light)
          .normal(0f, 0f, 1f);
    }
}
