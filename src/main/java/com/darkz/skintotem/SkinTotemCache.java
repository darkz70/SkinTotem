package com.darkz.skintotem;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SkinTotemCache {

    // Max entries in LRU cache
    private static final int MAX_CACHE = 64;

    // key -> generated 16x16 totem image
    private static final Map<String, BufferedImage> imageCache = new LinkedHashMap<>(MAX_CACHE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, BufferedImage> eldest) {
            return size() > MAX_CACHE;
        }
    };

    // Prevent duplicate in-flight requests
    private static final ConcurrentHashMap<String, CompletableFuture<BufferedImage>> pending = new ConcurrentHashMap<>();

    public static boolean hasCached(String key) {
        synchronized (imageCache) {
            return imageCache.containsKey(normalizeKey(key));
        }
    }

    public static BufferedImage getCached(String key) {
        synchronized (imageCache) {
            return imageCache.get(normalizeKey(key));
        }
    }

    public static CompletableFuture<BufferedImage> getOrFetch(String input) {
        String key = normalizeKey(input);

        synchronized (imageCache) {
            if (imageCache.containsKey(key)) {
                return CompletableFuture.completedFuture(imageCache.get(key));
            }
        }

        return pending.computeIfAbsent(key, k ->
            SkinFetcher.fetchSkin(input).thenApply(skin -> {
                BufferedImage totemImg = TotemTextureGenerator.generate(skin);
                if (totemImg != null) {
                    synchronized (imageCache) {
                        imageCache.put(k, totemImg);
                    }
                }
                pending.remove(k);
                return totemImg;
            })
        );
    }

    public static void invalidate(String key) {
        synchronized (imageCache) {
            imageCache.remove(normalizeKey(key));
        }
    }

    private static String normalizeKey(String input) {
        return input.trim().toLowerCase();
    }
}
