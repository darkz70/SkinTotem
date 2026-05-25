package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

@Environment(EnvType.CLIENT)
public class SkinTotemTextureManager {

    // key (normalized input) -> registered texture id
    private static final Map<String, Identifier> registeredIds = new ConcurrentHashMap<>();
    // currently loading (to avoid duplicate requests)
    private static final Set<String> loading = ConcurrentHashMap.newKeySet();

    public static Identifier getOrLoad(String input) {
        String key = normalize(input);
        Identifier existing = registeredIds.get(key);
        if (existing != null) return existing;

        if (loading.add(key)) {
            SkinFetcher.fetch(input)
                .thenApply(TotemTextureGenerator::generate)
                .thenAccept(img -> register(key, img));
        }

        return null; // returns null until texture is ready (default totem renders)
    }

    private static void register(String key, BufferedImage img) {
        if (img == null) {
            SkinTotemMod.LOGGER.warn("[SkinTotem] Skin image null for key: {}", key);
            loading.remove(key);
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            loading.remove(key);
            return;
        }

        // Convert BufferedImage -> PNG bytes -> NativeImage on main thread
        client.execute(() -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "PNG", baos);
                byte[] pngBytes = baos.toByteArray();

                NativeImage nativeImage = NativeImage.read(new ByteArrayInputStream(pngBytes));
                NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);

                // id format: skintotem:skin/<safekey>
                String safe = key.replaceAll("[^a-z0-9._-]", "_");
                Identifier id = Identifier.of(SkinTotemMod.MOD_ID, "skin/" + safe);

                client.getTextureManager().registerTexture(id, texture);
                registeredIds.put(key, id);
                SkinTotemMod.LOGGER.info("[SkinTotem] Registered texture for: {}", key);
            } catch (Exception e) {
                SkinTotemMod.LOGGER.error("[SkinTotem] Failed to register texture for {}: {}", key, e.getMessage());
            } finally {
                loading.remove(key);
            }
        });
    }

    public static String normalize(String input) {
        return input == null ? "" : input.trim().toLowerCase();
    }
}
