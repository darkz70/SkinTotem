package spichka.skintotem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SimpleTextureLoader {
    private static final Map<String, Identifier> TEXTURES = new HashMap<>();

    public static void loadTextures() {
        File cacheDir = new File(MinecraftClient.getInstance().runDirectory, "config/skintotem/skins");
        if (cacheDir.exists()) {
            File[] files = cacheDir.listFiles((dir, name) -> name.endsWith(".png"));
            if (files != null) {
                for (File file : files) {
                    String username = file.getName().replace(".png", "");
                    SkinLoader.loadSkin(username);
                }
            }
        }
    }

    public static void registerTexture(String username, NativeImage image) {
        Identifier id = Identifier.of(SkinTotem.MOD_ID, "textures/item/totem_" + username.toLowerCase());
        MinecraftClient.getInstance().getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
        TEXTURES.put(username, id);
    }

    public static Identifier getTexture(String username) {
        return TEXTURES.get(username);
    }
}
