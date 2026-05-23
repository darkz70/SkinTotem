package spichka.skintotem.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import spichka.skintotem.skin.SkinManager;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(
            method = "render",
            at = @At("TAIL"),
            require = 0
    )
    private void skintotem$cacheSkin(
            ItemStack stack,
            ModelTransformationMode mode,
            boolean leftHanded,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay,
            CallbackInfo ci
    ) {

        if (stack == null || stack.isEmpty()) {
            return;
        }

        NbtComponent data =
                stack.get(DataComponentTypes.CUSTOM_DATA);

        if (data == null) {
            return;
        }

        NbtCompound nbt = data.copyNbt();

        if (!nbt.containsUuid("Owner")) {
            return;
        }

        UUID owner = nbt.getUuid("Owner");

        // SAFE CACHE ONLY
        SkinManager.getSkin(owner);
    }
}
