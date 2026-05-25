package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

public class SkinFetcher {

    public static CompletableFuture<BufferedImage> fetch(String input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (isUrl(input)) return fromUrl(input);
                return byUsername(input);
            } catch (Exception e) {
                SkinTotemMod.LOGGER.warn("[SkinTotem] fetch failed for '{}': {}", input, e.getMessage());
                return null;
            }
        });
    }

    private static boolean isUrl(String s) {
        return s.startsWith("http://") || s.startsWith("https://");
    }

    public static BufferedImage fromUrl(String urlStr) throws IOException {
        HttpURLConnection conn = open(urlStr);
        try (InputStream is = conn.getInputStream()) {
            return ImageIO.read(is);
        }
    }

    private static BufferedImage byUsername(String username) {
        // 1. Mojang
        try {
            String profileJson = get("https://api.mojang.com/users/profiles/minecraft/" + username);
            if (profileJson != null && !profileJson.isEmpty()) {
                String uuid = JsonParser.parseString(profileJson).getAsJsonObject().get("id").getAsString();
                String sessionJson = get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
                if (sessionJson != null) {
                    for (var prop : JsonParser.parseString(sessionJson).getAsJsonObject().getAsJsonArray("properties")) {
                        var p = prop.getAsJsonObject();
                        if ("textures".equals(p.get("name").getAsString())) {
                            String decoded = new String(Base64.getDecoder().decode(p.get("value").getAsString()));
                            String skinUrl = JsonParser.parseString(decoded).getAsJsonObject()
                                    .getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
                            return fromUrl(skinUrl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("[SkinTotem] Mojang miss for {}: {}", username, e.getMessage());
        }

        // 2. Ely.by
        try {
            return fromUrl("https://skinsystem.ely.by/skins/" + username + ".png");
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("[SkinTotem] Ely.by miss for {}: {}", username, e.getMessage());
        }

        // 3. TLauncher
        try {
            String json = get("https://auth.tlauncher.org/skin/profile/texture/login/" + username);
            if (json != null) {
                var obj = JsonParser.parseString(json).getAsJsonObject();
                if (obj.has("SKIN")) {
                    return fromUrl(obj.getAsJsonObject("SKIN").get("url").getAsString());
                }
            }
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("[SkinTotem] TLauncher miss for {}: {}", username, e.getMessage());
        }

        return null;
    }

    private static String get(String urlStr) throws IOException {
        HttpURLConnection conn = open(urlStr);
        if (conn.getResponseCode() != 200) return null;
        try (InputStream is = conn.getInputStream()) {
            return new String(is.readAllBytes());
        }
    }

    private static HttpURLConnection open(String urlStr) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("User-Agent", "SkinTotem/2.1");
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(8000);
        return conn;
    }
}
