package com.darkz.skintotem.mixin;

import com.darkz.skintotem.SkinTotemCache;
import com.darkz.skintotem.SkinTotemMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Intercepts totem item rendering and replaces the texture
 * with one generated from the player skin stored in the item name.
 */
@Mixin(net.minecraft.client.render.item.ItemRenderer.class)
public class TotemRenderMixin {

    // Cache of registered dynamic texture identifiers
    private static final Map<String, Identifier> registeredTextures = new HashMap<>();

    /**
     * Hook into getModel to redirect the totem texture before rendering.
     * We register a dynamic texture on the texture manager.
     */
    @ModifyVariable(
        method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At("HEAD"),
        argsOnly = true,
        index = 1
    )
    private ItemStack skintotem$redirectTexture(ItemStack stack) {
        if (stack == null || !stack.isOf(Items.TOTEM_OF_UNDYING)) return stack;

        var nameComponent = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (nameComponent == null) return stack;

        String input = nameComponent.getString();
        if (input == null || input.isBlank()) return stack;

        String key = input.trim().toLowerCase();

        // If not cached yet, trigger async fetch (will render default until ready)
        if (!SkinTotemCache.hasCached(key)) {
            SkinTotemCache.getOrFetch(input).thenAccept(img -> {
                if (img != null) {
                    registerDynamicTexture(key, img);
                }
            });
            return stack;
        }

        // Already cached: make sure it's registered as dynamic texture
        if (!registeredTextures.containsKey(key)) {
            BufferedImage img = SkinTotemCache.getCached(key);
            if (img != null) {
                registerDynamicTexture(key, img);
            }
        }

        return stack;
    }

    private static void registerDynamicTexture(String key, BufferedImage img) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        String texId = "skintotem_" + key.replaceAll("[^a-z0-9_.-]", "_");
        Identifier id = Identifier.of(SkinTotemMod.MOD_ID, texId);

        if (registeredTextures.containsKey(key)) return;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "PNG", baos);
            byte[] bytes = baos.toByteArray();

            client.execute(() -> {
                try {
                    NativeImage nativeImage = NativeImage.read(new ByteArrayInputStream(bytes));
                    NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
                    client.getTextureManager().registerTexture(id, texture);
                    registeredTextures.put(key, id);
                } catch (Exception e) {
                    SkinTotemMod.LOGGER.warn("Failed to register dynamic totem texture: {}", e.getMessage());
                }
            });
        } catch (Exception e) {
            SkinTotemMod.LOGGER.warn("Failed to convert skin image: {}", e.getMessage());
        }
    }

    public static Identifier getRegisteredTexture(String key) {
        return registeredTextures.get(key != null ? key.trim().toLowerCase() : null);
    }
}
