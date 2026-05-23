package spichka.skintotem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class SkinLoader {
    private static final File CACHE_DIR = new File(MinecraftClient.getInstance().runDirectory, "config/skintotem/skins");

    public static void loadSkin(String username) {
        if (SimpleTextureLoader.getTexture(username) != null) return;

        CompletableFuture.runAsync(() -> {
            try {
                if (!CACHE_DIR.exists()) CACHE_DIR.mkdirs();
                File cacheFile = new File(CACHE_DIR, username + ".png");
                NativeImage skin = null;

                if (cacheFile.exists()) {
                    try (InputStream is = new FileInputStream(cacheFile)) {
                        skin = NativeImage.read(is);
                    } catch (Exception e) {
                        SkinTotem.LOGGER.error("Failed to read cached skin for " + username, e);
                    }
                }

                if (skin == null) {
                    skin = fetchSkin(username);
                    if (skin != null) {
                        try (FileOutputStream fos = new FileOutputStream(cacheFile)) {
                            fos.write(skin.getBytes());
                        } catch (Exception e) {
                            SkinTotem.LOGGER.error("Failed to cache skin for " + username, e);
                        }
                    }
                }

                if (skin != null) {
                    final NativeImage finalSkin = skin;
                    NativeImage totem = TotemGenerator.generateTotemFromSkin(finalSkin);
                    MinecraftClient.getInstance().execute(() -> {
                        SimpleTextureLoader.registerTexture(username, totem);
                        finalSkin.close();
                    });
                }
            } catch (Exception e) {
                SkinTotem.LOGGER.error("Failed to load skin for " + username, e);
            }
        });
    }

    private static NativeImage fetchSkin(String username) {
        String[] sources = {
            "https://skinmc.net/api/v1/totem/" + username,
            "http://skinsystem.ely.by/textures/" + username,
            "http://auth.tlauncher.org/skin/profile/texture/login/" + username
        };

        for (String source : sources) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(source).openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                if (conn.getResponseCode() == 200) {
                    try (InputStream is = conn.getInputStream()) {
                        return NativeImage.read(is);
                    }
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
}
