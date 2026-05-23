package spichka.skintotem.client;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import org.joml.Matrix4f;

import java.util.UUID;

import spichka.skintotem.skin.SkinCache;

public class TotemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    @Override
    public void render(
            ItemStack stack,
            net.minecraft.client.render.model.json.ModelTransformationMode mode,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay
    ) {

        NbtComponent data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data == null) return;

        NbtCompound nbt = data.copyNbt();
        if (!nbt.contains("Owner")) return;

        UUID owner = nbt.getUuid("Owner");

        Identifier skin = SkinCache.get(owner);
        if (skin == null) return;

        VertexConsumer consumer =
                vertexConsumers.getBuffer(RenderLayer.getEntityCutout(skin));

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float s = 0.2f;

        // ===== ГОЛОВА =====
        draw(consumer, matrix, light,
                -s, s,
                s, s * 3,
                8, 8,
                16, 16);

        // ===== ТЕЛО =====
        draw(consumer, matrix, light,
                -s, -s,
                s, s,
                20, 20,
                28, 32);

        // ===== ЛЕВАЯ РУКА =====
        draw(consumer, matrix, light,
                -s * 2, -s,
                -s, s,
                44, 20,
                48, 32);

        // ===== ПРАВАЯ РУКА =====
        draw(consumer, matrix, light,
                s, -s,
                s * 2, s,
                44, 20,
                48, 32);
    }

    private void draw(
            VertexConsumer c,
            Matrix4f m,
            int light,
            float x1, float y1,
            float x2, float y2,
            float u1, float v1,
            float u2, float v2
    ) {
        float U1 = u1 / 64f;
        float V1 = v1 / 64f;
        float U2 = u2 / 64f;
        float V2 = v2 / 64f;

        c.vertex(m, x1, y1, 0);
        c.color(255,255,255,255);
        c.texture(U1, V2);
        c.light(light);

        c.vertex(m, x2, y1, 0);
        c.color(255,255,255,255);
        c.texture(U2, V2);
        c.light(light);

        c.vertex(m, x2, y2, 0);
        c.color(255,255,255,255);
        c.texture(U2, V1);
        c.light(light);

        c.vertex(m, x1, y2, 0);
        c.color(255,255,255,255);
        c.texture(U1, V1);
        c.light(light);
    }
}
        UUID owner = nbt.getUuid("Owner");

        Identifier skin = SkinCache.get(owner);
        if (skin == null) return;

        VertexConsumer consumer =
        vertexConsumers.getBuffer(RenderLayer.getEntityCutout(skin));

Matrix4f matrix = matrices.peek().getPositionMatrix();

// 1
consumer.vertex(matrix, -0.5f, -0.5f, 0);
consumer.color(255, 255, 255, 255);
consumer.texture(0, 1);
consumer.light(light);

// 2
consumer.vertex(matrix, 0.5f, -0.5f, 0);
consumer.color(255, 255, 255, 255);
consumer.texture(1, 1);
consumer.light(light);

// 3
consumer.vertex(matrix, 0.5f, 0.5f, 0);
consumer.color(255, 255, 255, 255);
consumer.texture(1, 0);
consumer.light(light);

// 4
consumer.vertex(matrix, -0.5f, 0.5f, 0);
consumer.color(255, 255, 255, 255);
consumer.texture(0, 0);
consumer.light(light);
        }
}
