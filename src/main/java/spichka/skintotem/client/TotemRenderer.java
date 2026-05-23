package spichka.skintotem.client;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
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

        consumer.vertex(matrix, -0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(0, 1)
                .light(light)
                .endVertex();

        consumer.vertex(matrix, 0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(1, 1)
                .light(light)
                .endVertex();

        consumer.vertex(matrix, 0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(1, 0)
                .light(light)
                .endVertex();

        consumer.vertex(matrix, -0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(0, 0)
                .light(light)
                .endVertex();
    }
}
