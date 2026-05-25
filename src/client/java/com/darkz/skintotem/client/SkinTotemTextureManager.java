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

public class SkinTotemTextureManager {

    private static final Map<String, Identifier> ready   = new ConcurrentHashMap<>();
    private static final Set<String>             loading = ConcurrentHashMap.newKeySet();

    /** Returns the registered texture ID, or null if still loading (fetch kicked off). */
    public static Identifier getOrLoad(String input) {
        String key = normalize(input);
        Identifier id = ready.get(key);
        if (id != null) return id;

        if (loading.add(key)) {
            SkinFetcher.fetch(input)
                .thenApply(TotemTextureGenerator::generate)
                .thenAccept(img -> register(key, img));
        }
        return null;
    }

    private static void register(String key, BufferedImage img) {
        if (img == null) {
            SkinTotemMod.LOGGER.warn("[SkinTotem] null image for key: {}", key);
            loading.remove(key);
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) { loading.remove(key); return; }

        mc.execute(() -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "PNG", baos);
                NativeImage ni = NativeImage.read(new ByteArrayInputStream(baos.toByteArray()));
                NativeImageBackedTexture tex = new NativeImageBackedTexture(ni);
                String safe = key.replaceAll("[^a-z0-9._\\-]", "_");
                Identifier id = Identifier.of(SkinTotemMod.MOD_ID, "skin/" + safe);
                mc.getTextureManager().registerTexture(id, tex);
                ready.put(key, id);
                SkinTotemMod.LOGGER.info("[SkinTotem] Registered texture: {}", id);
            } catch (Exception e) {
                SkinTotemMod.LOGGER.error("[SkinTotem] register failed for {}: {}", key, e.getMessage());
            } finally {
                loading.remove(key);
            }
        });
    }

    public static String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}
