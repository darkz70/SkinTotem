package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
public class SkinFetcher {

    public static CompletableFuture<BufferedImage> fetch(String input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (isUrl(input)) {
                    return fromUrl(input);
                }
                return byUsername(input);
            } catch (Exception e) {
                SkinTotemMod.LOGGER.warn("[SkinTotem] Failed to fetch '{}': {}", input, e.getMessage());
                return null;
            }
        });
    }

    private static boolean isUrl(String s) {
        return s.startsWith("http://") || s.startsWith("https://");
    }

    public static BufferedImage fromUrl(String urlStr) throws IOException {
        HttpURLConnection conn = openConnection(urlStr);
        try (InputStream is = conn.getInputStream()) {
            return ImageIO.read(is);
        }
    }

    private static BufferedImage byUsername(String username) throws IOException {
        // 1. Mojang
        try {
            String profileJson = getString("https://api.mojang.com/users/profiles/minecraft/" + username);
            if (profileJson != null && !profileJson.isEmpty()) {
                var obj = JsonParser.parseString(profileJson).getAsJsonObject();
                String uuid = obj.get("id").getAsString();
                String sessionJson = getString("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
                if (sessionJson != null) {
                    var sess = JsonParser.parseString(sessionJson).getAsJsonObject();
                    for (var prop : sess.getAsJsonArray("properties")) {
                        var p = prop.getAsJsonObject();
                        if ("textures".equals(p.get("name").getAsString())) {
                            String decoded = new String(Base64.getDecoder().decode(p.get("value").getAsString()));
                            var tex = JsonParser.parseString(decoded).getAsJsonObject();
                            String skinUrl = tex.getAsJsonObject("textures")
                                    .getAsJsonObject("SKIN")
                                    .get("url").getAsString();
                            return fromUrl(skinUrl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("[SkinTotem] Mojang failed for {}: {}", username, e.getMessage());
        }

        // 2. Ely.by
        try {
            return fromUrl("https://skinsystem.ely.by/skins/" + username + ".png");
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("[SkinTotem] Ely.by failed for {}: {}", username, e.getMessage());
        }

        // 3. TLauncher
        try {
            String json = getString("https://auth.tlauncher.org/skin/profile/texture/login/" + username);
            if (json != null) {
                var obj = JsonParser.parseString(json).getAsJsonObject();
                if (obj.has("SKIN")) {
                    String skinUrl = obj.getAsJsonObject("SKIN").get("url").getAsString();
                    return fromUrl(skinUrl);
                }
            }
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("[SkinTotem] TLauncher failed for {}: {}", username, e.getMessage());
        }

        return null;
    }

    private static String getString(String urlStr) throws IOException {
        HttpURLConnection conn = openConnection(urlStr);
        if (conn.getResponseCode() != 200) return null;
        try (InputStream is = conn.getInputStream()) {
            return new String(is.readAllBytes());
        }
    }

    private static HttpURLConnection openConnection(String urlStr) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("User-Agent", "SkinTotem/2.0");
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(8000);
        return conn;
    }
}
