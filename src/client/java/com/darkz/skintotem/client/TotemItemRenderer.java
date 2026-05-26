package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

/**
 * Custom BIIR renderer for Totem of Undying.
 * Uses BuiltinModelItemRenderer (Fabric BIIR API) — no mixins, fully Indigo-compatible.
 *
 * When the totem has a custom name (nick or URL), we fetch the skin texture and
 * draw a flat 16x16 quad using that texture instead of the default totem model.
 * When no name is set, we delegate to the vanilla item renderer.
 */
public class TotemItemRenderer extends BuiltinModelItemRenderer {

    // Default totem texture (vanilla)
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

        // Determine which texture to use
        Identifier texId = DEFAULT_TOTEM;

        var nameComp = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (nameComp != null) {
            String input = nameComp.getString();
            if (input != null && !input.isBlank()) {
                Identifier skinTex = SkinTotemCache.getOrLoad(input);
                if (skinTex != null) {
                    texId = skinTex;
                }
            }
        }

        // Draw flat quad with the chosen texture
        drawFlatItem(matrices, vertexConsumers, light, overlay, texId);
    }

    /**
     * Renders a flat 1x1 quad (centred on 0,0) with the given texture.
     * Matches how vanilla renders 2D item sprites.
     */
    private static void drawFlatItem(MatrixStack matrices,
                                     VertexConsumerProvider vertexConsumers,
                                     int light, int overlay,
                                     Identifier texture) {
        matrices.push();

        // Centre the quad
        matrices.translate(0.5f, 0.5f, 0.5f);

        RenderLayer layer = RenderLayer.getItemEntityTranslucentCull(texture);
        VertexConsumer vc = vertexConsumers.getBuffer(layer);
        Matrix4f mat = matrices.peek().getPositionMatrix();

        // Two triangles forming a 1x1 quad, facing +Z
        // Positions span -0.5 to +0.5 on X and Y
        float x0 = -0.5f, x1 = 0.5f;
        float y0 = -0.5f, y1 = 0.5f;
        float z  =  0.0f;

        // normal pointing toward viewer (+Z)
        float nx = 0, ny = 0, nz = 1;

        // Quad vertices (counter-clockwise)
        vertex(vc, mat, x0, y1, z, 0, 0, nx, ny, nz, overlay, light);
        vertex(vc, mat, x0, y0, z, 0, 1, nx, ny, nz, overlay, light);
        vertex(vc, mat, x1, y0, z, 1, 1, nx, ny, nz, overlay, light);
        vertex(vc, mat, x1, y1, z, 1, 0, nx, ny, nz, overlay, light);

        matrices.pop();
    }

    private static void vertex(VertexConsumer vc, Matrix4f mat,
                                float x, float y, float z,
                                float u, float v,
                                float nx, float ny, float nz,
                                int overlay, int light) {
        vc.vertex(mat, x, y, z)
          .color(255, 255, 255, 255)
          .texture(u, v)
          .overlay(overlay)
          .light(light)
          .normal(nx, ny, nz);
    }
}
