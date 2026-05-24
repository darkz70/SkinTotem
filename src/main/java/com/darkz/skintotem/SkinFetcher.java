package com.darkz.skintotem;

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

    // Returns a 64x64 skin BufferedImage given a nickname or a URL/link.
    public static CompletableFuture<BufferedImage> fetchSkin(String input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (isUrl(input)) {
                    return fetchImageFromUrl(input);
                } else {
                    return fetchSkinByUsername(input);
                }
            } catch (Exception e) {
                SkinTotemMod.LOGGER.warn("Failed to fetch skin for '{}': {}", input, e.getMessage());
                return null;
            }
        });
    }

    private static boolean isUrl(String s) {
        return s.startsWith("http://") || s.startsWith("https://");
    }

    private static BufferedImage fetchImageFromUrl(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "SkinTotem/1.0");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        try (InputStream is = conn.getInputStream()) {
            return ImageIO.read(is);
        }
    }

    private static BufferedImage fetchSkinByUsername(String username) throws IOException {
        // Try Mojang first
        BufferedImage img = tryMojang(username);
        if (img != null) return img;

        // Try Ely.by
        img = tryElyBy(username);
        if (img != null) return img;

        // Try TLauncher (uses Ely.by-compatible API)
        return tryTLauncher(username);
    }

    private static BufferedImage tryMojang(String username) {
        try {
            // Step 1: UUID lookup
            String profileUrl = "https://api.mojang.com/users/profiles/minecraft/" + username;
            String profileJson = httpGet(profileUrl);
            if (profileJson == null || profileJson.isEmpty()) return null;

            var profileObj = JsonParser.parseString(profileJson).getAsJsonObject();
            String uuid = profileObj.get("id").getAsString();

            // Step 2: Profile with texture
            String sessionUrl = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
            String sessionJson = httpGet(sessionUrl);
            if (sessionJson == null) return null;

            var sessionObj = JsonParser.parseString(sessionJson).getAsJsonObject();
            var properties = sessionObj.getAsJsonArray("properties");
            for (var prop : properties) {
                var propObj = prop.getAsJsonObject();
                if ("textures".equals(propObj.get("name").getAsString())) {
                    String encoded = propObj.get("value").getAsString();
                    String decoded = new String(Base64.getDecoder().decode(encoded));
                    var texObj = JsonParser.parseString(decoded).getAsJsonObject();
                    String skinUrl = texObj.getAsJsonObject("textures")
                            .getAsJsonObject("SKIN")
                            .get("url").getAsString();
                    return fetchImageFromUrl(skinUrl);
                }
            }
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("Mojang lookup failed for {}: {}", username, e.getMessage());
        }
        return null;
    }

    private static BufferedImage tryElyBy(String username) {
        try {
            String url = "https://skinsystem.ely.by/skins/" + username + ".png";
            return fetchImageFromUrl(url);
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("Ely.by lookup failed for {}: {}", username, e.getMessage());
            return null;
        }
    }

    private static BufferedImage tryTLauncher(String username) {
        try {
            // TLauncher uses a compatible skin server
            String url = "https://auth.tlauncher.org/skin/profile/texture/login/" + username;
            String json = httpGet(url);
            if (json == null) return null;
            var obj = JsonParser.parseString(json).getAsJsonObject();
            if (obj.has("SKIN")) {
                String skinUrl = obj.getAsJsonObject("SKIN").get("url").getAsString();
                return fetchImageFromUrl(skinUrl);
            }
        } catch (Exception e) {
            SkinTotemMod.LOGGER.debug("TLauncher lookup failed for {}: {}", username, e.getMessage());
        }
        return null;
    }

    private static String httpGet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "SkinTotem/1.0");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        int code = conn.getResponseCode();
        if (code != 200) return null;
        try (InputStream is = conn.getInputStream()) {
            return new String(is.readAllBytes());
        }
    }
}
