package spichka.skintotem.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SkinFetcher {

    private static final Map<String, Identifier> CACHE = new ConcurrentHashMap<>();

    public static void loadSkinAsync(String username) {
        if (CACHE.containsKey(username)) return;

        new Thread(() -> {
            try {
                String uuidJson = read("https://api.mojang.com/users/profiles/minecraft/" + username);
                JsonObject uuidObj = JsonParser.parseString(uuidJson).getAsJsonObject();
                String uuid = uuidObj.get("id").getAsString();

                String profileJson = read("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
                JsonObject profile = JsonParser.parseString(profileJson).getAsJsonObject();

                String value = profile
                        .getAsJsonArray("properties")
                        .get(0).getAsJsonObject()
                        .get("value").getAsString();

                String decoded = new String(Base64.getDecoder().decode(value));
                JsonObject textureJson = JsonParser.parseString(decoded).getAsJsonObject();

                String skinUrl = textureJson
                        .getAsJsonObject("textures")
                        .getAsJsonObject("SKIN")
                        .get("url").getAsString();

                InputStream stream = new URL(skinUrl).openStream();
                NativeImage image = NativeImage.read(stream);

                MinecraftClient.getInstance().execute(() -> {
                    Identifier id = new Identifier("skintotem", username);
                    MinecraftClient.getInstance().getTextureManager()
                            .registerTexture(id, new NativeImageBackedTexture(image));

                    CACHE.put(username, id);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Identifier getSkin(String username) {
        return CACHE.get(username);
    }

    private static String read(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream in = conn.getInputStream();
        return new String(in.readAllBytes());
    }
}
