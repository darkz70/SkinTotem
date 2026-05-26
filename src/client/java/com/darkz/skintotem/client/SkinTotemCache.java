package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

public class SkinTotemCache {

    private static final Map<String, Identifier> READY   = new ConcurrentHashMap<>();
    private static final Set<String>             LOADING = ConcurrentHashMap.newKeySet();

    public static Identifier getOrLoad(String input) {
        if (input == null || input.isBlank()) return null;
        String key = normalize(input);

        Identifier id = READY.get(key);
        if (id != null) return id;

        if (LOADING.add(key)) {
            SkinFetcher.fetch(input)
                .thenApply(TotemTextureGenerator::generate)
                .thenAccept(img -> registerOnMainThread(key, img));
        }
        return null;
    }

    private static void registerOnMainThread(String key, BufferedImage img) {
        if (img == null) {
            SkinTotemMod.LOGGER.warn("[SkinTotem] null image for '{}'", key);
            LOADING.remove(key);
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) { LOADING.remove(key); return; }

        mc.execute(() -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "PNG", baos);
                NativeImage ni = NativeImage.read(new ByteArrayInputStream(baos.toByteArray()));
                NativeImageBackedTexture tex = new NativeImageBackedTexture(ni);
                String safe = key.replaceAll("[^a-z0-9._\\-]", "_");
                Identifier id = Identifier.of(SkinTotemMod.MOD_ID, "skin/" + safe);
                mc.getTextureManager().registerTexture(id, tex);
                READY.put(key, id);
                SkinTotemMod.LOGGER.info("[SkinTotem] Texture ready: {}", id);
            } catch (Exception e) {
                SkinTotemMod.LOGGER.error("[SkinTotem] register failed '{}': {}", key, e.getMessage());
            } finally {
                LOADING.remove(key);
            }
        });
    }

    public static String normalize(String s) {
        return s.trim().toLowerCase();
    }
}
