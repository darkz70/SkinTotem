package spichka.skintotem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SimpleTextureLoader {

    private static final Map<String, Identifier> textures = new HashMap<>();

    public static void loadTextures() {
        Path skinTotemDir = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("skintotem");
        if (!Files.exists(skinTotemDir)) {
            try {
                Files.createDirectories(skinTotemDir);
            } catch (IOException e) {
                SkinTotem.LOGGER.error("Failed to create config directory for SkinTotem: {}", e.getMessage());
                return;
            }
        }

        try {
            Files.list(skinTotemDir)
                    .filter(path -> path.toString().endsWith(".png"))
                    .forEach(path -> {
                        String filename = path.getFileName().toString();
                        String nickname = filename.substring(0, filename.length() - ".png".length());
                        loadDynamicTexture(nickname);
                    });
        } catch (IOException e) {
            SkinTotem.LOGGER.error("Error scanning skinTotem directory: {}", e.getMessage());
        }
    }

    public static void loadDynamicTexture(String username) {
        Path skinTotemDir = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("skintotem");
        File skinFile = skinTotemDir.resolve(username + ".png").toFile();

        if (!skinFile.exists()) {
            SkinTotem.LOGGER.warn("Attempted to load dynamic texture for {} but file does not exist: {}", username, skinFile.getAbsolutePath());
            return;
        }

        try (InputStream is = new FileInputStream(skinFile)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            if (bufferedImage == null) {
                SkinTotem.LOGGER.error("Failed to read image for {} from {}. ImageIO returned null.", username, skinFile.getAbsolutePath());
                return;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", baos);
            InputStream bais = new ByteArrayInputStream(baos.toByteArray());

            NativeImage nativeImage = NativeImage.read(bais);
            NativeImageBackedTexture dynamicTexture = new NativeImageBackedTexture(nativeImage);
            Identifier id = Identifier.of(SkinTotem.MOD_ID, "totem_skins/" + username.toLowerCase());

            MinecraftClient.getInstance().getTextureManager().registerTexture(id, dynamicTexture);
            textures.put(username, id);
            SkinTotem.LOGGER.info("Registered dynamic texture for {}: {}", username, id);
        } catch (IOException e) {
            SkinTotem.LOGGER.error("Error loading dynamic texture for {}: {}", username, e.getMessage());
        }
    }

    public static Identifier getTexture(String username) {
        return textures.get(username);
    }
}
