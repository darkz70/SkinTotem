package spichka.skintotem.client;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.util.UUID;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import spichka.skintotem.skin.SkinCache;

public class TotemRenderer {

    public static void render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vcp, int light) {

        NbtComponent data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data == null) return;

        NbtCompound nbt = data.copyNbt();
        if (!nbt.contains("Owner")) return;

        UUID owner = nbt.getUuid("Owner");

        Identifier skin = SkinCache.get(owner);
        if (skin == null) return;

        VertexConsumer c = vcp.getBuffer(RenderLayer.getEntityCutout(skin));

        matrices.push();
        matrices.scale(0.5f, 0.5f, 0.5f);

        Matrix4f m = matrices.peek().getPositionMatrix();

        // quad
        c.vertex(m, -0.5f, -0.5f, 0).color(255,255,255,255).texture(0,1).light(light);
        c.vertex(m,  0.5f, -0.5f, 0).color(255,255,255,255).texture(1,1).light(light);
        c.vertex(m,  0.5f,  0.5f, 0).color(255,255,255,255).texture(1,0).light(light);
        c.vertex(m, -0.5f,  0.5f, 0).color(255,255,255,255).texture(0,0).light(light);

        matrices.pop();
    }
}
