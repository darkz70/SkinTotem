package spichka.skintotem.client;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.client.render.model.json.ModelTransformationMode;

public class SkinTotemItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    @Override
    public void render(ItemStack stack,
                       ModelTransformationMode mode,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       int overlay) {

        TotemRenderer.render(stack, matrices, vertexConsumers, light);
    }
}
