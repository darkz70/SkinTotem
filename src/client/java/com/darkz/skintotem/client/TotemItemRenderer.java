package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class TotemItemRenderer extends BuiltinModelItemRenderer {

    private static final Identifier DEFAULT_TOTEM =
            Identifier.of("minecraft", "textures/item/totem_of_undying.png");

    public TotemItemRenderer() {
        super(MinecraftClient.getInstance().getBlockEntityRenderDispatcher(),
              MinecraftClient.getInstance().getEntityModelLoader());
    }

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

        drawFlat(matrices, vertexConsumers, light, overlay, texId);
    }

    private static void drawFlat(MatrixStack matrices,
                                  VertexConsumerProvider vcp,
                                  int light, int overlay,
                                  Identifier texture) {
        matrices.push();
        matrices.translate(0.5f, 0.5f, 0.5f);

        VertexConsumer vc = vcp.getBuffer(RenderLayer.getItemEntityTranslucentCull(texture));
        Matrix4f m = matrices.peek().getPositionMatrix();

        quad(vc, m, -0.5f, 0.5f, -0.5f, 0.5f, 0f, overlay, light);

        matrices.pop();
    }

    private static void quad(VertexConsumer vc, Matrix4f m,
                              float x0, float x1, float y0, float y1, float z,
                              int overlay, int light) {
        v(vc, m, x0, y1, z, 0, 0, overlay, light);
        v(vc, m, x0, y0, z, 0, 1, overlay, light);
        v(vc, m, x1, y0, z, 1, 1, overlay, light);
        v(vc, m, x1, y1, z, 1, 0, overlay, light);
    }

    private static void v(VertexConsumer vc, Matrix4f m,
                           float x, float y, float z,
                           float u, float v,
                           int overlay, int light) {
        vc.vertex(m, x, y, z)
          .color(255, 255, 255, 255)
          .texture(u, v)
          .overlay(overlay)
          .light(light)
          .normal(0, 0, 1);
    }
}
