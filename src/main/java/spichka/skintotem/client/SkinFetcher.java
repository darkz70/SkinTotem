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
                // 1. UUID
                String uuidJson = read("https://api.mojang.com/users/profiles/minecraft/" + username);
                JsonObject uuidObj = JsonParser.parseString(uuidJson).getAsJsonObject();
                String uuid = uuidObj.get("id").getAsString();

                // 2. skin data
                String profileJson = read("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
                JsonObject profileObj = JsonParser.parseString(profileJson).getAsJsonObject();

                String value = profileObj.getAsJsonArray("properties")
                        .get(0).getAsJsonObject()
                        .get("value").getAsString();

                String decoded = new String(Base64.getDecoder().decode(value));
                JsonObject textureObj = JsonParser.parseString(decoded)
                        .getAsJsonObject()
                        .getAsJsonObject("textures")
                        .getAsJsonObject("SKIN");

                String skinUrl = textureObj.get("url").getAsString();

                // 3. download skin
                InputStream in = new URL(skinUrl).openStream();
                NativeImage image = NativeImage.read(in);

                Identifier id = new Identifier("skintotem", "skin_" + username.toLowerCase());

                MinecraftClient.getInstance().execute(() -> {
                    MinecraftClient.getInstance().getTextureManager()
                            .registerTexture(id, new NativeImageBackedTexture(image));
                });

                CACHE.put(username, id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Identifier getSkin(String username) {
        return CACHE.get(username);
    }

    private static String read(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        InputStream in = conn.getInputStream();
        return new String(in.readAllBytes());
    }
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
