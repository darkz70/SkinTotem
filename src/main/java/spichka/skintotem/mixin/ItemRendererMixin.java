package spichka.skintotem.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spichka.skintotem.SimpleTextureLoader;
import spichka.skintotem.SkinLoader;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(
            method = {"render", "method_23177"},
            at = @At("HEAD"),
            cancellable = true
    )
    private void skintotem$render(
            ItemStack stack,
            ModelTransformationMode renderMode,
            boolean leftHanded,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay,
            BakedModel model,
            CallbackInfo ci
    ) {
        if (stack == null || stack.isEmpty()) return;
        if (stack.getItem() != Items.TOTEM_OF_UNDYING) return;

        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) return;

        NbtCompound nbt = customData.copyNbt();
        String username = null;
        if (nbt.contains("username")) username = nbt.getString("username");
        else if (nbt.contains("Owner")) username = nbt.getString("Owner");

        if (username == null || username.isEmpty()) return;

        Identifier texId = SimpleTextureLoader.getTexture(username);

        if (texId == null) {
            SkinLoader.loadSkin(username);
            return;
        }

        matrices.push();
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texId));
        ((ItemRendererAccessor)(Object)this).invokeRenderBakedItemModel(model, stack, light, overlay, matrices, consumer);
        matrices.pop();
        ci.cancel();
    }
}
