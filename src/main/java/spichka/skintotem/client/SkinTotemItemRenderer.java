package spichka.skintotem.client;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class SkinTotemItemRenderer implements DynamicItemRenderer {

    private static final Identifier TEXTURE =
            Identifier.of("minecraft", "textures/entity/steve.png");

    @Override
    public void render(ItemStack stack,
                       ModelTransformationMode mode,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       int overlay) {

        matrices.push();

        VertexConsumer consumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutout(TEXTURE)
        );

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();

        float s = 0.2f;

        // ГОЛОВА
        draw(consumer, matrix, light,
                -s,  s,   s,  s + 0.2f,
                8, 8, 16, 16);

        // ТЕЛО
        draw(consumer, matrix, light,
                -s, -s,   s,  s,
                20, 20, 28, 32);

        // ЛЕВАЯ НОГА
        draw(consumer, matrix, light,
                -s, -s - 0.3f,   0,  -s,
                4, 20, 8, 32);

        // ПРАВАЯ НОГА
        draw(consumer, matrix, light,
                 0, -s - 0.3f,   s, -s,
                8, 20, 12, 32);

        matrices.pop();
    }

    private void draw(VertexConsumer c, Matrix4f m, int light,
                      float x1, float y1, float x2, float y2,
                      int u1, int v1, int u2, int v2) {

        float U1 = u1 / 64f;
        float V1 = v1 / 64f;
        float U2 = u2 / 64f;
        float V2 = v2 / 64f;

        c.vertex(m, x1, y1, 0).color(255,255,255,255).texture(U1, V2).light(light);
        c.vertex(m, x2, y1, 0).color(255,255,255,255).texture(U2, V2).light(light);
        c.vertex(m, x2, y2, 0).color(255,255,255,255).texture(U2, V1).light(light);
        c.vertex(m, x1, y2, 0).color(255,255,255,255).texture(U1, V1).light(light);
    }
}
