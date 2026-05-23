package spichka.skintotem.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
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
                // 1. username -> uuid
                URL uuidUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
                JsonObject uuidJson = JsonParser.parseReader(new InputStreamReader(uuidUrl.openStream())).getAsJsonObject();
                String uuid = uuidJson.get("id").getAsString();

                // 2. uuid -> skin data
                URL profileUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
                JsonObject profileJson = JsonParser.parseReader(new InputStreamReader(profileUrl.openStream())).getAsJsonObject();

                String value = profileJson
                        .getAsJsonArray("properties")
                        .get(0).getAsJsonObject()
                        .get("value").getAsString();

                String decoded = new String(Base64.getDecoder().decode(value));
                JsonObject textureJson = JsonParser.parseString(decoded).getAsJsonObject();

                String skinUrl = textureJson
                        .getAsJsonObject("textures")
                        .getAsJsonObject("SKIN")
                        .get("url").getAsString();

                // 3. скачать текстуру
                NativeImage image = NativeImage.read(new URL(skinUrl).openStream());

                MinecraftClient.getInstance().execute(() -> {
                    NativeImageBackedTexture tex = new NativeImageBackedTexture(image);
                    Identifier id = MinecraftClient.getInstance()
                            .getTextureManager()
                            .registerDynamicTexture("skin_" + username, tex);

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
}
