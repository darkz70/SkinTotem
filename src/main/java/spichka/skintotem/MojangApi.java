package spichka.skintotem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.texture.NativeImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MojangApi {
    private static final int TIMEOUT = 5000;

    public static CompletableFuture<NativeImage> fetchSkin(String nickname) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                NativeImage img = fetchImage("https://skinmc.net/api/v1/totem/" + nickname);
                if (img != null) return img;

                UUID uuid = getUuid(nickname);
                if (uuid != null) {
                    String url = getSkinUrl(uuid);
                    if (url != null) return fetchImage(url);
                }

                img = fetchImage("http://skinsystem.ely.by/textures/" + nickname);
                if (img != null) return img;

                return fetchImage("http://auth.tlauncher.org/skin/profile/texture/login/" + nickname);
            } catch (Exception e) {
                return null;
            }
        });
    }

    private static NativeImage fetchImage(String urlString) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(urlString).openConnection();
        c.setConnectTimeout(TIMEOUT);
        c.setReadTimeout(TIMEOUT);
        if (c.getResponseCode() == 200) {
            try (InputStream is = c.getInputStream()) {
                return NativeImage.read(is);
            }
        }
        return null;
    }

    private static UUID getUuid(String nickname) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + nickname).openConnection();
        if (c.getResponseCode() != 200) return null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
            JsonObject json = JsonParser.parseReader(in).getAsJsonObject();
            String id = json.get("id").getAsString();
            return UUID.fromString(id.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
    }

    private static String getSkinUrl(UUID uuid) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid).openConnection();
        if (c.getResponseCode() != 200) return null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
            JsonObject json = JsonParser.parseReader(in).getAsJsonObject();
            for (JsonElement p : json.getAsJsonArray("properties")) {
                if (p.getAsJsonObject().get("name").getAsString().equals("textures")) {
                    String decoded = SkinDecoder.decodeBase64(p.getAsJsonObject().get("value").getAsString());
                    return JsonParser.parseString(decoded).getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
                }
            }
        }
        return null;
    }
}
