package spichka.skintotem.client;

import net.minecraft.client.render.*;
import net.minecraft.client.render.item.BuiltinItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SkinTotemItemRenderer implements BuiltinItemRenderer {

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode,
                       MatrixStack matrices, VertexConsumerProvider providers,
                       int light, int overlay) {

        String username = "Notch"; // 🔥 потом можешь брать из NBT

        SkinFetcher.loadSkinAsync(username);
        Identifier skin = SkinFetcher.getSkin(username);

        if (skin == null) return; // ещё грузится

        VertexConsumer consumer = providers.getBuffer(
                RenderLayer.getEntityCutout(skin)
        );

        matrices.push();
        matrices.scale(1.0f, 1.0f, 1.0f);

        MatrixStack.Entry entry = matrices.peek();

        // 🟩 голова (из скина)
        drawPart(consumer, entry, light,
                -0.3f, -0.3f, 0.3f, 0.3f,
                8, 8, 16, 16); // UV головы

        // 🟫 тело
        drawPart(consumer, entry, light,
                -0.25f, 0.3f, 0.25f, 0.8f,
                20, 20, 28, 32);

        // ✋ руки
        drawPart(consumer, entry, light,
                -0.6f, 0.3f, -0.3f, 0.8f,
                44, 20, 48, 32);

        drawPart(consumer, entry, light,
                0.3f, 0.3f, 0.6f, 0.8f,
                44, 20, 48, 32);

        matrices.pop();
    }

    private void drawPart(VertexConsumer c, MatrixStack.Entry e, int light,
                          float x1, float y1, float x2, float y2,
                          int u1, int v1, int u2, int v2) {

        float U1 = u1 / 64f;
        float V1 = v1 / 64f;
        float U2 = u2 / 64f;
        float V2 = v2 / 64f;

        var m = e.getPositionMatrix();
        var n = e.getNormalMatrix();

        c.vertex(m, x1, y1, 0).color(255,255,255,255).texture(U1,V2)
                .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(n,0,0,1).next();

        c.vertex(m, x2, y1, 0).color(255,255,255,255).texture(U2,V2)
                .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(n,0,0,1).next();

        c.vertex(m, x2, y2, 0).color(255,255,255,255).texture(U2,V1)
                .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(n,0,0,1).next();

        c.vertex(m, x1, y2, 0).color(255,255,255,255).texture(U1,V1)
                .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(n,0,0,1).next();
    }
}
