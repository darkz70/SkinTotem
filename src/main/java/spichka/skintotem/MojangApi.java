package spichka.skintotem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MojangApi {

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;

    public static CompletableFuture<BufferedImage> getTotemOrSkinFromAnySource(String nickname) {
        return CompletableFuture.supplyAsync(() -> {
            BufferedImage image = null;
            try {
                image = fetchImageFromUrl("https://skinmc.net/api/v1/totem/" + nickname);
                if (image != null) return image;
            } catch (IOException ignored) {}

            try {
                UUID uuid = getUuidFromMojang(nickname);
                if (uuid != null) {
                    String skinUrl = getSkinUrlFromMojang(uuid);
                    if (skinUrl != null) {
                        image = fetchImageFromUrl(skinUrl);
                        if (image != null) return image;
                    }
                }
            } catch (IOException ignored) {}

            try {
                image = fetchImageFromUrl("http://skinsystem.ely.by/textures/" + nickname);
                if (image != null) return image;
            } catch (IOException ignored) {}

            try {
                image = fetchImageFromUrl("http://auth.tlauncher.org/skin/profile/texture/login/" + nickname);
                if (image != null) return image;
            } catch (IOException ignored) {}

            return null;
        });
    }

    private static BufferedImage fetchImageFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return ImageIO.read(connection.getInputStream());
        }
        return null;
    }

    private static UUID getUuidFromMojang(String nickname) throws IOException {
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + nickname;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) content.append(line);
            in.close();

            JsonObject jsonObject = JsonParser.parseString(content.toString()).getAsJsonObject();
            String uuidString = jsonObject.get("id").getAsString();
            return UUID.fromString(uuidString.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
        return null;
    }

    private static String getSkinUrlFromMojang(UUID uuid) throws IOException {
        String urlString = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) content.append(line);
            in.close();

            JsonObject jsonObject = JsonParser.parseString(content.toString()).getAsJsonObject();
            for (JsonElement property : jsonObject.getAsJsonArray("properties")) {
                JsonObject propObj = property.getAsJsonObject();
                if (propObj.get("name").getAsString().equals("textures")) {
                    String value = propObj.get("value").getAsString();
                    String decoded = SkinDecoder.decodeBase64(value);
                    JsonObject texturesObj = JsonParser.parseString(decoded).getAsJsonObject();
                    if (texturesObj.has("textures") && texturesObj.getAsJsonObject("textures").has("SKIN")) {
                        return texturesObj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
                    }
                }
            }
        }
        return null;
    }
}
